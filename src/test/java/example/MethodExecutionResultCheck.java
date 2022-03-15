package example;

import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.core.VoidResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class MethodExecutionResultCheck {

    void method(boolean failure)  {
        if (failure) throw new IllegalArgumentException();
    }

    VoidResult methodNotEx(boolean failure) {
        if (failure) return Result.err();
        return Result.ok();
    }


    public int methodCheck(boolean failure) {
        try {
            method(failure);
        } catch (RuntimeException e) {
            return -1;
        }

        return 0;

    }

    public int methodCheckNotEx(boolean failure) {
        return methodNotEx(failure)
                .resolve(0, -1);
    }

    @Test
    public void methodCheckTest() {
        assertEquals(-1, methodCheck(true));
        assertEquals(0, methodCheck(false));

        assertEquals(-1, methodCheckNotEx(true));
        assertEquals(0, methodCheckNotEx(false));
    }
}
