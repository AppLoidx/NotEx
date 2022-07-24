package example.lookup;

import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.core.VoidResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Objects;

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

    // LOOKUP MUTATION EXAMPLE (WIP) -----------

    private static class ErrorObject {
        private String error;

        ErrorObject(String error) {
            this.error = error;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ErrorObject that = (ErrorObject) o;
            return Objects.equals(error, that.error);
        }

        @Override
        public int hashCode() {
            return Objects.hash(error);
        }
    }

    private VoidResult<ErrorObject> check() {
        return Result.err(new ErrorObject("Some Error"));
    }

    @Test
    public void lookupMutation() {
        check()
                .lookup(errorObject -> errorObject.error = "There is no error");
    }
}
