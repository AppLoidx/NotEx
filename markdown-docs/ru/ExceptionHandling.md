# Exception Handling

**Проблема**: Существуют проекты, в которых так или иначе используются исключения. В случае, если они используются
неправильно, то их следует заменить на использование NotEx. Но с другой стороны, некоторые проекты могут потребовать
больших расходов для переписывания существующей кодовой базы.

**Решение**: Использовать обертки над методами, которые выбрасывают исключение и возвращать
`Result` объект от NotEx

## В действии

Предположим, что есть метод, который может выбросить исключение при арифметической ошибке.

```java
Integer methodWithException(){
        throw new ArithmeticException();
}
```

Тогда следует предполагать, что использовалось оно примерно в таком виде через try-catch

```java
try {
    return methodWithException();
} catch(ArithmeticException e) {
    return onError(e);
}
```

Такой код может вызвать проблемы при использовании с `Result` даже если не изменять поведение
метода `methodWithException()`. Необходимо будет завернуть в `Result` два случая, разделенных
через `try-catch`.

Чтобы это не вызывало больших проблем при интеграции, а именно старого кода (без использования NotEx)
и нового (с использованием NotEx) существует класс `ExceptionWrapperUtil`

Выглядит его использование следующим образом:

```java
Integer methodWithException() {
    throw new ArithmeticException();
}

public void exceptionHandle() {
    Result<Integer, String> result =
            wrapException(this::methodWithException, Throwable::getMessage);

    assertFalse(result.isOk());

}

```

```java
private final static Integer RETURN_RESULT = 5;

Integer methodWithException(boolean isThrow) {
    if (isThrow)
        throw new ArithmeticException();
    else
        return RETURN_RESULT;
}

public void exceptionHandleWithArg() {
    Result<Integer, String> resultBad =
            wrapException(() -> methodWithException(true), Throwable::getMessage);

    assertFalse(resultBad.isOk());

    Result<Integer, String> resultGood =
            wrapException(() -> methodWithException(false), Throwable::getMessage);

    assertTrue(resultGood.isOk());
    assertEquals(RETURN_RESULT, resultGood.getObject());

}
```