package example;


import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class FinallyTest {
    @Test
    public void basicResultUsage() {
        Boolean value = Result.<String, String>ok("Hey1!!")
                .resolveFrom(String::length)
                .apply(this::assertOdd)
                .lookup(log::error)
                .apply(this::assertTrue)
                .resolve(errorObject -> {
                    errorObject.getErrorObject().ifPresent(log::info);
                    return false;
                }, () -> log.info("Finally"));

        log.info(value.toString());
    }

    private Result<Boolean, String> assertOdd(int number) {
        if (number % 2 == 0) {
            return Result.err(Notification.of("Number is not odd at all!"));
        } else {
            return Result.ok(true);
        }
    }

    private Result<Boolean, String> assertTrue(Boolean bool) {
        if (bool) {
            return Result.ok(true);
        } else {
            return Result.err(Notification.of("This result contains error!"));

        }
    }
}
