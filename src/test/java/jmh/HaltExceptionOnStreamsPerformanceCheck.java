package jmh;

import com.apploidxxx.notex.Notification;
import com.apploidxxx.notex.Result;
import com.apploidxxx.notex.ResultWrappers;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(5)
public class HaltExceptionOnStreamsPerformanceCheck {
    private static final Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6);
    private static final List<Integer> defaultList = List.of();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HaltExceptionOnStreamsPerformanceCheck.class.getSimpleName())
                .forks(5)
                .build();

        new Runner(opt).run();
    }

    @Param({"0", "5", "10", "20"})
    public int depth;

    @Benchmark
    public List<Integer> std() {
        return stdExample(depth);
    }

    @Benchmark
    public List<Integer> halt() {
        return haltExample(depth);
    }

    private List<Integer> haltExample(int depth) {
        try {
            return ResultWrappers
                    .streamHaltIfError(stream
                            .map(n -> doSomethingWithNotEx(n, depth)))
                    .collect(Collectors.toList());

        } catch (Exception ignore) {
            return defaultList;
        }
    }

    private List<Integer> stdExample(int depth) {
        try {
            return stream
                    .map(n -> doSomething(n, depth))
                    .collect(Collectors.toList()); // exception
        } catch (Exception ignore) {
            return defaultList;
        }
    }

    private Integer doSomething(Integer number) {
        if (number % 3 == 0) {
            throw new IllegalArgumentException("number is dividable by 3");
        }

        return number + 1;
    }

    private Result<Integer, String> doSomethingWithNotEx(Integer number) {
        if (number % 3 == 0) {
            return Result.of(Notification.of("number is dividable by 3"));
        }

        return Result.of(number + 1);
    }

    private Integer doSomething(Integer number, int depth) {
        if (depth == 0) {
            return doSomething(number);
        } else {
            return doSomething(number, depth - 1);
        }
    }

    private Result<Integer, String> doSomethingWithNotEx(Integer number, int depth) {
        if (depth == 0) {
            return doSomethingWithNotEx(number);
        } else {
            return doSomethingWithNotEx(number, depth - 1);
        }
    }


}
