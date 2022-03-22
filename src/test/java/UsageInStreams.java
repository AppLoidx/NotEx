import com.apploidxxx.notex.HaltException;
import com.apploidxxx.notex.ResultWrappers;
import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;

@Slf4j
public class UsageInStreams {

    private static final Supplier<Stream<Integer>> stream = () -> Stream.of(1, 2, 3, 4, 5, 6);


    @Test
    public void standard() {
        assertThrows("standard implementation with exception",
                IllegalArgumentException.class,
                () -> {
                    List<Integer> list = stream.get().map(this::doSomething).collect(Collectors.toList()); // exception
                    log.info(list.toString());
                });

    }


    @Test
    public void withNotEx() {

        // skip invalid values
        List<Integer> list = stream.get().map(this::doSomethingWithNotEx)
                .filter(Result::isOk)
                .map(Result::getObject)
                .collect(Collectors.toList());


        log.info(list.toString());  // [1, 2, 4, 5]
    }

    @Test
    public void withNotExReplaceValues() {
        // replace invalid values
        List<Integer> list = stream.get().map(this::doSomethingWithNotEx)
                .map(r -> r.resolve(notification -> 0))
                .collect(Collectors.toList());


        log.info(list.toString());  // [1, 2, 0, 4, 5, 0]
    }

    @Test
    public void withNotExHalt() {

        assertThrows("Halt with static exception",
                HaltException.class,
                () -> {
                    // halt with static exception
                    List<Integer> list =
                            ResultWrappers.streamHaltIfError(stream.get()
                                            .map(this::doSomethingWithNotEx))
                                    .collect(Collectors.toList()); // exception
                    log.info(list.toString());
                });


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
