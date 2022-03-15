package jmh;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(5)
public class DepthStackCallPerformanceCheck {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DepthStackCallPerformanceCheck.class.getSimpleName())
                .forks(5)
                .build();

        new Runner(opt).run();
    }

    @Param({"0", "5", "10", "20", "50", "100", "150"})
    public int depth;

    @Benchmark
    public Integer benchException() {
        int value;

        try {
            value = withException(depth);
        } catch (ArithmeticException exception) {
            value = valueIfExceptionOccurs();
        }

        return value;
    }

    @Benchmark
    public Integer benchNotEx() {
        return withNotEx(depth).resolve(notification -> valueIfExceptionOccurs());
    }

    private Integer withException(int depth) {
        if (depth == 0)
            throw new ArithmeticException();
        else
            return withException(depth - 1);
    }
    private Result<Integer, String> withNotEx(int depth) {
        if (depth == 0)
            return Result.err(Notification.of("Arithmetic error"));
        else
            return withNotEx(depth - 1);
    }

    private int valueIfExceptionOccurs() {
        return 5;
    }


}
