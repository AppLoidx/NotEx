# Error Inheritance

Одним из преимуществ использования `try-catch` блока является возможность по разному обрабатывать
конкретные исключительные ситуации

Пример

```java
public void stdException() {
    final int expectedError = -2;
    int err = 0;
    
    try {
        someDo(ErrorType.SECOND);
    } catch (IllegalArgumentException e) {
        err = -1;
    } catch (ArithmeticException e) {
        err = -2;
    }
    
    assertEquals(expectedError, err);
}
```

В NotEx можно сделать подобную обработку конкретных ошибок в функциональном стиле с использованием
наследования типов ошибок

```java
class ErrorObject { }

class IllegalError extends ErrorObject { }
class ArithmeticError extends ErrorObject { }
```

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
