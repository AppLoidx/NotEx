



Нельзя делать такое из-за стирания типов

```java
@Test
public void notExErrorHandling() {
    final int expectedError = -2;
    int err = 0;

    someDoNotEx(ErrorType.SECOND)
            .solve(0, e -> {
                if (e instanceof IllegalError) {
                    return -1;
                } else if (e instanceof ArithmeticError) {
                    return -2;
                }
                return 0;
            });

    assertEquals(expectedError, err);
}
```