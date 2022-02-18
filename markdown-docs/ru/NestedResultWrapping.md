# Nested Result Wrapping

**Проблема**: Использование `Result` может быть вложенным и необходимо создать комфортные
условия для создания цепочек из вызовов, каждый метод из которых возвращает `Result`

**Решение**: Создание архитектуры вокруг вызова методов

## В действии

Вложенный вызов методов, каждый из которых возвращает `Result` может выглядеть не очень красиво,
если использовать стандартный подход создания переменных и через `if-else` создавать от него
ответвления

Пример ужасного кода:
```java
final String val = "Hey1!!";
final int number = val.length();

Result<Boolean, String> assertOddRes = assertOdd(number);
if (assertOddRes.isOk()) {
    Boolean resObject = assertOddRes.getObject();
    Result<Boolean, String> assertTrueRes = assertTrue(resObject);
    if (assertTrueRes.isOk()) {
        log.info("Answer: " + assertTrueRes.getObject());
    } else {
        log.info("This result contains error!");
    }
} else {
    log.info("Number is not odd at all!");
}
```

Каждый раз когда мы вызываем какой-то метод, то берем из него объект `Result` и проверяем
условие `isOk()`, что приводит к вложенным `if-else` и общей сложности кода

У `Result` есть метод `apply`, который позволяет применить какую-либо функцию к объекту,
находящемуся внутри обертки:

При его использовании код выглядит чище и понятней

```java
Boolean value = Result.<String, String>ok("Hey1!!")
        .resolveFrom(String::length)
        .apply(this::assertOdd)
        .apply(this::assertTrue)
        .resolve(val -> val, errorObject -> {
            log.info(errorObject);
            return false;
        });

log.info(value.toString());
```

В таком коде отчетливо можно увидеть какой метод после какого вызывается.