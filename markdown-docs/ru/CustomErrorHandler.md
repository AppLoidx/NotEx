# Custom Error Handler

**Проблема**: Передача кастомного обработчика ошибки

**Решение**: Иногда может понадобится использование пользовательской обработки ошибки
при последовательном исполнении кода через метод `.apply`

Для это существует перегрузка метода `apply`, которая в аргумент принимает функцию-обработчик
ошибки. На вход она должна принимать нотификацию с объектом ошибки, а на возвращать
должна объект `Result`. При этом `Result` может как содержать флаг ошибки, так и не содержать его

Ниже приведена сигнатура метода `apply`, где первый вызов функции фактически идентичен второму

```java
.apply(this::assertOdd)
```
```java
.apply(this::assertOdd, Result::err)
```

Рассмотрим стандартный код, в котором возникает ошибка в методе assertOdd
```java
@Test
public void goodUsage() {
    Boolean value = Result.<String, String>ok("Hey1!!")
            .resolveFrom(String::length)
            .apply(this::assertOdd) // error occurs
            .apply(this::assertTrue)
            .resolve(val -> val, notification -> {
                log.info(notification.getErrorObject().orElse("Error"));
                return false;
            });

    log.info(value.toString());
}
```

В таком случае, в методе resolve залогируется ошибка из метода `assertOdd`

На этом примере изменим код так, чтобы даже при возникновении ошибки, результат не
содержал ошибки:

```java
@Test
public void goodUsage() {
    Boolean value = Result.<String, String>ok("Hey1!!")
            .resolveFrom(String::length)
            .apply(this::assertOdd, notification -> Result.ok(true))
            .apply(this::assertTrue)
            .resolve(val -> val, notification -> {
                log.info(notification.getErrorObject().orElse("Error"));
                return false;
            });

    log.info(value.toString());
}
```

Здесь видно как мы передаем обработчик ошибки, который всегда возвращает `Result.ok`.
```java
.apply(this::assertOdd, notification -> Result.ok(true))
```

Таким образом, можно по ходу выполнения кода изменять её поведение на более высоком уровне 
при этом не меняя логику самих методов.
