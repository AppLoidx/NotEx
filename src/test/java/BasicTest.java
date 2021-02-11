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
        Boolean resolve = Result.of("Hey!!")
                .resolveFrom(String::length)
                .resolve(this::assertOdd)
                .resolve(this::assertTrue)
                .resolve(val -> val, notification -> {
                    log.info(notification.getMessage());
                    return false;
                });

        log.info(resolve.toString());
    }

    private <T> Result<Boolean, T> assertOdd(int number) {
        if (number % 2 == 0) {
            return Result.of(Notification.of("Number is not odd at all!"));
        } else {
            return Result.of(true);
        }
    }

    private <S> Result<Boolean, S> assertTrue(Boolean bool) {
        if (bool) {
            return Result.of(true);
        } else {
            return Result.of(Notification.of("This result contains error!"));

        }
    }

}
