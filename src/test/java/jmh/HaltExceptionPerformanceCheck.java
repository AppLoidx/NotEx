package jmh;

import com.apploidxxx.notex.HaltException;
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
public class HaltExceptionPerformanceCheck {
    private static final HaltException haltException = new HaltException();

    private static final int value = 13;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HaltExceptionPerformanceCheck.class.getSimpleName())
                .forks(5)
                .build();

        new Runner(opt).run();
    }

    public int doSome_HaltException() {
        throw haltException;
    }

    public int doSome_StdException() {
        throw new IllegalArgumentException();
    }

    public int depthStdException(int depth) {
        if (depth == 0) {
            return doSome_StdException();
        } else {
            return depthStdException(depth - 1);
        }
    }

    public int depthHaltException(int depth) {
        if (depth == 0) {
            return doSome_HaltException();
        } else {
            return depthHaltException(depth - 1);
        }
    }

    @Param({"0", "10", "20", "40", "80", "160", "320"})
    public int depth;

    @Benchmark
    public int haltException() {
        try {
            return depthHaltException(depth);
        } catch (Exception exp) {
            return value;
        }
    }

    @Benchmark
    public int stdException() {
        try {
            return depthStdException(depth);
        } catch (Exception exp) {
            return value;
        }
    }

}
