package jmh;

import com.apploidxxx.notex.Notification;
import com.apploidxxx.notex.Result;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;

@State(Scope.Benchmark)
public class BasicPerformanceCheck {
    @Fork(value = 1, warmups = 1)
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

    @Fork(value = 1, warmups = 1)
    @Benchmark
    public Integer benchNotEx() {
        return withNotEx().resolve(notification -> valueIfExceptionOccurs());
    }

    private Integer withException() {
        throw new ArithmeticException();
    }
    private Result<Integer, String> withNotEx() {
        return Result.of(Notification.of("Arithmetic error"));
    }

    private int valueIfExceptionOccurs() {
        return 5;
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
