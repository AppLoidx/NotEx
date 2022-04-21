# Lookup

Иногда при возникновении какой-либо ошибки пользователь хочет получить доступ к нему, например, чтобы залогировать его

В случае использования исключений это не вызывает осложнений, если мы хотим получить информацию об ошибке в `catch` блоке

```java
try {
    checkWrapper();
} catch (RuntimeException re) {
    log.error("Error: ", re);
}
```

Но при вложенных вызовах может возникнуть "проблема локализации".

Рассмотрим, упрощенный пример:

```java
public void check1Exp() {
    throw new RuntimeException("Info about error");
}

public void checkWrapper() {
    check1Exp();    // we want to log error here
}

@Test
public void rethrowExample() {
    try {
        checkWrapper();
    } catch (RuntimeException re) {
        log.error("Error: ", re);
    }
}
```

Допустим, что мы хотим узнать какая ошибка произошла при вызове `check1Exp()` внутри метода `checkWrapper`. В таком случае, если мы просто поймаем ошибку в `try-catch`, то Runtime не будет дальше искать обработчика ошибки, так как он уже его нашел. Следовательно, нам нужно еще раз бросить это исключение:

```java
public void check1Exp() {
    throw new RuntimeException("Info about error");
}

public void checkWrapperWithLookup() {
    try {
        check1Exp();
    } catch (RuntimeException re) {
        log.error("Error lookup: " + re.getMessage());
        throw re;   // re-throwing the error
    }
}

@Test
public void rethrowExample() {
    
    try {
        checkWrapperWithLookup();
    } catch (RuntimeException re) {
        log.error("Error: ", re);
    }

}
```

В таком случае, мы получаем нагроможденный код, в котором еще внутри `catch` блока кидается (повторно) исключение, чтобы не прервать поиск обработчика.

## Почему возникла проблема локализации

Может возникнуть вопрос почему такая проблема есть, ведь можно добавить логирование на уровень выше и там получить доступ к информации об ошибке.

Но здесь есть две проблемы, которые могут возникнуть при таком способе решения

Во-первых, обработчик находящийся выше по стеку вызовов может обрабатывать не только те исключения, которые выбрасывает наш метод. Он может обрабатывать сразу несколько ошибок от нескольких методов. 

![Handler diagram](https://i.imgur.com/iEMbMtS.png)

Пытаясь залогировать одно возникновение ошибки мы можем случайно залогировать все другие ошибки

Во-вторых, что звучит еще серьезнее мы можем не иметь доступа к обработчику ошибки. Вполне возможно, что `try-catch` будет расположен в библиотеке, к которому у нас нет доступа. Например, `ResponseStatusException` из Spring. Данное исключение обрабатывается самим фреймворком и без дополнительных усилий мы не сможем изменить поведение обработки данной ошибки.

## Lookup в NotEx

В NotEx такая проблема решается методом `lookup`, который позволяет получить доступ к объекту ошибки

```java
public VoidResult<String> check1Exp() {
    return Result.err("Info about error");
}

public VoidResult<String> checkWrapperWithLookup() {
    return check1Exp()
            .lookup(err -> log.error("Error lookup: " + err));
}

@Test
public void rethrowExample() {
    checkWrapperWithLookup().solve(err -> log.error("Error: " + err));
}
```

Метод `lookup` ничего не делает кроме использования функции (без возвращаемого значения) на объекте об ошибке:

```java
public VoidResult<S> lookup(Solvable<S> lookup) {
    lookup(lookup, () -> {});
    return this;
}

public VoidResult<S> lookup(Solvable<S> lookup, Runnable onNoError) {
    getNotification()
            .flatMap(Notification::getErrorObject)
            .ifPresentOrElse(lookup::solve, onNoError);
    return this;
}
```

Такой lookup можно легко встроить в цепочку вызовов `Result`:

```java
Boolean value = Result.<String, String>ok("Hey1!!")
    .resolveFrom(String::length)
    .apply(this::assertOdd)
    .lookup(log::info)
    .apply(this::assertTrue)
    .resolve(val -> val, notification -> {
        log.info(notification.getErrorObject().orElse("Error"));
        return false;
    });
```

И также в случае, если ошибки нет - можно вызвать Runnable, что может быть полезно в дебаге

```java
return check1Exp()
    .lookup(err -> log.error("Error lookup: " + err), 
            () -> log.info("all is fine"));
```

## Мутация объекта

Единственной условностью и проблемой метода `lookup` является мутирование объекта об ошибке. Чтобы метод `lookup` работал правильно и по назначению - объект ошибки не должен быть мутирован.

Мы можем задать это как контракт: 
> Объект об ошибке не должен быть мутирован при вызове метода lookup.

Тогда возникает следующая проблема - необходимо соблюсти контракт и оповещать пользователя, если он нарушил этот контракт. Что забавно, в это случае мы должны кинуть **исключение**, так как это является **нарушением контракта**, то есть **непредвиденной** ошибкой

В предыдущем примере не было такой проблемы, так как объект об ошибке был String. Но проблема сразу бросается в глаза, если объектом об ошибке является объект, который содержит изменяемые поля

```java
private static class ErrorObject {
    private String error;

    ErrorObject(String error) {
        this.error = error;
    }
    
    // getter, setter
    
}
```

На самом деле, такая проблема решается легко - просто необходимо писать иммутабельные классы. В этом случае объявить `error` как `final` решило бы проблему. Но так как классы объектов об ошибке пишет пользователь, то необходимо проследить за исполнением контракта.

Можно прийти к простому решению: сравнивать хэш-код до и после вызова метода lookup:

```java
int originalHashCode = errObj.hashCode();
lookup.solve(errObj);
if (originalHashCode != errObj.hashCode()) {
    throw new IllegalArgumentException("Error object mutated within lookup call");
}
```

Но здесь есть несколько проблем, с которыми можно столкнуться:

Во-первых, это коллизия хэшей. Контракт equals и hashCode гласит, что если метод `equals` утверждает, что два объекта не равны один другому, это не означает, что метод hashCode возвратит для них разные числа. То есть вполне может быть так, что на самом деле, разные объекты вернут одинаковый хэш-код из-за чего исключение не выбросится

Второе касается реализации метода hashCode по умолчанию. В классе Object `hashCode()` возвращает целочисленное представление адреса памяти объекта из-за чего нельзя говорить по хэшу, что объект был мутирован.

То есть, чтобы вторая проблема была решена нужно всегда переопределять `hashCode` как
функцию зависящую от всех полей класса, соответственно для соблюдения контракта equals тоже:

```java
private static class ErrorObject {
    private String error;

    ErrorObject(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorObject that = (ErrorObject) o;
        return Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }
}
```

Таким образом, проверка хэша не является решением для пресечения мутации объекта. 

Дальнейшая разработка упирается в три варианта решения:

- Реализовать создание копий объектов, чтобы передавать **их** в функцию;
- Уметь **достоверно** определять мутацию объекта;
- Не выявлять нарушения контракта;

