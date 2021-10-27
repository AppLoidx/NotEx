import com.apploidxxx.notex.Notification;
import com.apploidxxx.notex.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Basic usage of NotEx library
 */
@Slf4j
public class BasicTest {

    /**
     * Example test
     */
    @Test
    public void basicResultUsage() {
        Boolean value = Result.<String, String>of("Hey1!!")
            .resolveFrom(String::length)
            .apply(this::assertOdd)
            .apply(this::assertTrue)
            .resolve(val -> val, errorObject -> {
                log.info(errorObject);
                return false;
            });

        log.info(value.toString());
    }

    private Result<Boolean, String> assertOdd(int number) {
        if (number % 2 == 0) {
            return Result.of(Notification.of("Number is not odd at all!"));
        } else {
            return Result.of(true);
        }
    }

    private Result<Boolean, String> assertTrue(Boolean bool) {
        if (bool) {
            return Result.of(true);
        } else {
            return Result.of(Notification.of("This result contains error!"));

        }
    }

}
