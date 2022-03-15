package jmh;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(5)
public class BasicPerformanceCheck {

    @Benchmark
    public Integer benchException() {
        Integer value;

        try {
            value = withException();
        } catch (ArithmeticException exception) {
            value = valueIfExceptionOccurs();
        }

        return value;
    }

    @Benchmark
    public Integer benchNotEx() {
        return withNotEx().resolve(notification -> valueIfExceptionOccurs());
    }

    private Integer withException() {
        throw new ArithmeticException();
    }
    private Result<Integer, String> withNotEx() {
        return Result.err(Notification.of("Arithmetic error"));
    }

    private int valueIfExceptionOccurs() {
        return 5;
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
