package example.lookup;

import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.core.VoidResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ResultLookup {
    public VoidResult<String> check1Exp() {
        return Result.err("Info about error");
    }

    public VoidResult<String> checkWrapper() {
        return check1Exp();
    }

    public VoidResult<String> checkWrapperWithLookup() {
        return check1Exp()
                .lookup(err -> log.error("Error lookup: " + err));
    }

    @Test
    public void rethrowExample() {

        log.info("Standard call --------------------");

        checkWrapper().solve(err -> log.error("Error: " + err));

        log.info("Call with lookup -----------------");

        checkWrapperWithLookup().solve(err -> log.error("Error: " + err));


    }
}
