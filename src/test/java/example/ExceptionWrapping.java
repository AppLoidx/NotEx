package example;

import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.exception.ExceptionWrapperUtil;
import org.junit.Test;

import static com.apploidxxx.notex.exception.ExceptionWrapperUtil.wrapException;
import static org.junit.Assert.*;

public class ExceptionWrapping {

    private final static Integer RETURN_RESULT = 5;

    Integer methodWithException() {
        throw new ArithmeticException();
    }

Integer methodWithException(boolean isThrow) {

    if (isThrow)
        throw new ArithmeticException();
    else
        return RETURN_RESULT;
}


    @Test
    public void exceptionHandle() {
        Result<Integer, String> result =
                wrapException(this::methodWithException, Throwable::getMessage);

        assertFalse(result.isOk());

    }

    @Test
    public void exceptionHandleWithArg() {
        Result<Integer, String> resultBad =
                wrapException(() -> methodWithException(true), Throwable::getMessage);

        assertFalse(resultBad.isOk());

        Result<Integer, String> resultGood =
                wrapException(() -> methodWithException(false), Throwable::getMessage);

        assertTrue(resultGood.isOk());
        assertEquals(RETURN_RESULT, resultGood.getObject());

    }
}
