package example;

import com.apploidxxx.notex.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ErrorsInheritanceExample {

    private enum ErrorType {
        FIRST, SECOND;
    }

    private void someDo(ErrorType errorType) {
        if (errorType == ErrorType.FIRST) throw new IllegalArgumentException();
        else throw new ArithmeticException();
    }

    private static class ErrorObject {
    }

    private static class IllegalError extends ErrorObject {
    }

    private static class ArithmeticError extends ErrorObject {
    }

    private Result<Integer, ErrorObject> someDoNotEx(ErrorType errorType) {
        if (errorType == ErrorType.FIRST) return Result.err(new IllegalError());
        else return Result.err(new ArithmeticError());
    }

    @Test
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


    @Test
    public void notExErrorHandling() {
        final Integer expectedError = -2;
        Integer err;

        err = someDoNotEx(ErrorType.SECOND)
                .solve(0, e -> {
                    if (e.getClass().isAssignableFrom(IllegalError.class)) {
                        return -1;
                    } else if (ErrorsInheritanceExample.ArithmeticError.class.isAssignableFrom(e.getClass())) {
                        return -2;
                    }

                    return -3;
                });

        assertEquals(expectedError, err);
    }

@Test
public void notExErrorHandling_2() {
    final Integer expectedError = -2;

    Integer err = someDoNotEx(ErrorType.SECOND)
            .resolveFor(IllegalError.class, -1)
            .resolveFor(ArithmeticError.class, -2)
            .resolve(-3);

    assertEquals(expectedError, err);
}


}
