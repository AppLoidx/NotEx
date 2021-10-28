import com.apploidxxx.notex.Notification;
import com.apploidxxx.notex.Result;
import com.apploidxxx.notex.ResultWrappers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class UsageInStreams {

private static final Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6);



    @Test
    public void standard() {
        // standard implementation with exception
        List<Integer> list = stream.map(this::doSomething).collect(Collectors.toList()); // exception
        log.info(list.toString());
    }


    @Test
    public void withNotEx() {

        // skip invalid values
        List<Integer> list = stream.map(this::doSomethingWithNotEx)
                .filter(Result::isOk)
                .map(Result::getObject)
                .collect(Collectors.toList());


        log.info(list.toString());  // [1, 2, 4, 5]
    }

    @Test
    public void withNotExReplaceValues() {
        // replace invalid values
        List<Integer> list = stream.map(this::doSomethingWithNotEx)
                .map(r -> r.resolve(notification -> 0))
                .collect(Collectors.toList());


        log.info(list.toString());  // [1, 2, 0, 4, 5, 0]
    }

    @Test
    public void withNotExHalt() {
        // halt with static exception
        List<Integer> list =
                ResultWrappers.streamHaltIfError(stream
                        .map(this::doSomethingWithNotEx))
                .collect(Collectors.toList()); // exception
        log.info(list.toString());
    }

    private Integer doSomething(Integer number) {
        if (number % 3 == 0) {
            throw new IllegalArgumentException("number is dividable by 3");
        }

        return number + 1;
    }

    private Result<Integer, String> doSomethingWithNotEx(Integer number) {
        if (number % 3 == 0) {
            return Result.err(Notification.of(String.format("number %d is dividable by 3", number)));
        }

        return Result.ok(number);
    }
}
