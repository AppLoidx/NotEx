## Lookup

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

## Обработка различных типов ошибок

```java
public void notExErrorHandling() {
    final Integer expectedError = -2;
    Integer err = someDoNotEx(ErrorType.SECOND)
            .resolveFor(IllegalError.class, -1)
            .resolveFor(ArithmeticError.class, -2)
            .resolve( -3);

    assertEquals(expectedError, err);
}
```

## Кастомный обработчик ошибки

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

## Обертывание исключений

```java
    @Test
    public void exceptionHandle() {
        Result<Integer, String> result =
                wrapException(this::methodWithException, Throwable::getMessage);

        assertFalse(result.isOk());

    }
```


## Finally

```java
        Boolean value = Result.<String, String>ok("Hey1!!")
                .resolveFrom(String::length)
                .apply(this::assertOdd)
                .lookup(log::error)
                .apply(this::assertTrue)
                .resolve(errorObject -> {
                    errorObject.getErrorObject().ifPresent(log::info);
                    return false;
                }, () -> log.info("Finally"));
```

## Java Stream API

```java
        List<Integer> list = stream.get().map(this::doSomethingWithNotEx)
                .filter(Result::isOk)
                .map(Result::getObject)
                .collect(Collectors.toList());
```

