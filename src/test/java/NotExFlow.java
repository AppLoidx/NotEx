import com.apploidxxx.notex.Notification;
import com.apploidxxx.notex.Result;
import org.junit.Test;

public class NotExFlow {
    @Test
    public void flow() {
        Integer number = 0;

        String result = Result.<Integer, String>ok(number)
                .apply(this::minusOne)
                .apply(this::minusTwo)
                .apply(this::minusThree)
                .resolve(num -> "Number is " + num, errorObject -> errorObject);
        System.out.println(result);


    }


    private Result<Integer, String> minusOne(Integer number) {
        if (number < 1) {
            return Result.err(Notification.of("Negative error (1)"));
        } else {
            return Result.ok(number - 1);
        }
    }

    private Result<Integer, String> minusTwo(Integer number) {
        if (number < 2) {
            return Result.err(Notification.of("Negative error (2)"));
        } else {
            return Result.ok(number - 2);
        }
    }

    private Result<Integer, String> minusThree(Integer number) {
        if (number < 3) {
            return Result.err(Notification.of("Negative error (3)"));
        } else {
            return Result.ok(number - 3);
        }
    }
}
