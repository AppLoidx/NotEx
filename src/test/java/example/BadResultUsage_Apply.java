package example;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BadResultUsage_Apply {


    @Test
    public void goodUsage() {
        Boolean value = Result.<String, String>ok("Hey1!!")
                .resolveFrom(String::length)
                .apply(this::assertOdd)
                .apply(this::assertTrue)
                .resolve(val -> val, notification -> {
                    log.info(notification.getErrorObject().orElse("Error"));
                    return false;
                });

        log.info(value.toString());

    }


    @Test
    public void badUsage() {
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
