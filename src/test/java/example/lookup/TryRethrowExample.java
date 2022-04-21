package example.lookup;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TryRethrowExample {

    public void check1Exp() {
        throw new RuntimeException("Info about error");
    }

    public void checkWrapper() {
        check1Exp();
    }

    public void checkWrapperWithLookup() {
        try {
            check1Exp();
        } catch (RuntimeException re) {
            log.error("Error lookup: " + re.getMessage());
            throw re;   // re-throwing the error
        }
    }

    @Test
    public void rethrowExample() {

        log.info("Standard call --------------------");

        try {
            checkWrapper();
        } catch (RuntimeException re) {
            log.error("Error: ", re);
        }

        log.info("Call with lookup -----------------");

        try {
            checkWrapperWithLookup();
        } catch (RuntimeException re) {
            log.error("Error: ", re);
        }

    }
}
