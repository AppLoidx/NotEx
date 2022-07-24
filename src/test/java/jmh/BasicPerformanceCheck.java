package jmh;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(5)
public class BasicPerformanceCheck {

    private static final Integer ERROR_VAL = 5;

    @Benchmark
    public Integer benchException() {
        Integer value;

        try {
            value = withException();
        } catch (ArithmeticException exception) {
            value = ERROR_VAL;
        }

        return value;
    }

    @Benchmark
    public Integer benchNotEx() {
        return withNotEx().resolve(ERROR_VAL);
    }

    private Integer withException() {
        throw new ArithmeticException();
    }

    private Result<Integer, String> withNotEx() {
        return Result.err(Notification.of("Arithmetic error"));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BasicPerformanceCheck.class.getSimpleName())
                .forks(5)
                .build();

        new Runner(opt).run();
    }
}
