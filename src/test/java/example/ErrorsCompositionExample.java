package example;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.core.VoidResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ErrorsCompositionExample {

    private enum ErrorType {
        FIRST, SECOND;
    }

    private void someDo(ErrorType errorType) {
        if (errorType == ErrorType.FIRST) throw new IllegalArgumentException();
        else throw new ArithmeticException();
    }

    private static class ErrorObject {
        private Class<? extends ErrorObject> clazz;
        ErrorObject() {
            clazz = this.getClass();
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }

    private static class IllegalError extends ErrorObject {
    }

    private static class ArithmeticError extends ErrorObject {
    }

    private Result<Void, ErrorObject> someDoNotEx(ErrorType errorType) {
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
        final int expectedError = -2;
        int err = 0;

//        someDoNotEx(ErrorType.SECOND)
//                .solve(0, e -> {
//                    log.info(e.getClass().toString());
//                    if (e.getClazz() == IllegalError.class) {
//                        return -1;
//                    } else if (e.getClass(). == ErrorsCompositionExample.ArithmeticError.class) {
//                        return -2;
//                    }
//
//                    return 0;
//                });

        assertEquals(expectedError, err);
    }


}
