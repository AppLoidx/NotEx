import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.ValidationError;
import lombok.Getter;
import org.junit.Test;

public class Validation {


    @Test
    public void validation() {
        final UserFields userFields = new UserFields(1, -1, 1, -1);
        boolean valid = validate(userFields)
                .resolve(errorObject -> {
                    errorObject.getErrorObject()
                            .ifPresent(errObj -> errObj
                                    .onEachError((field, error) -> System.out.printf("Error on '%s' : %s%n", field, error)));
                    return false;
                });
    }

    private Result<Boolean, ValidationError<String, String>> validate(final UserFields userFields) {
        ValidationError<String, String> errorObject = new ValidationError<>();

        if (userFields.age < 0) {
            errorObject.addError("age", "Negative error");
        }

        if (userFields.year < 0) {
            errorObject.addError("year", "Negative error");
        }

        if (userFields.month < 0) {
            errorObject.addError("month", "Negative error");
        }

        if (userFields.day < 0) {
            errorObject.addError("day", "Negative error");
        }

        if (errorObject.isHasError()) {
            return Result.of(false, Notification.of(errorObject));
        } else {
            return Result.ok(true);
        }

    }

    private void validateException(final UserFields userFields) {
        ValidationError<String, String> errorObject = new ValidationError<>();

        if (userFields.age < 0) {
            throw new IllegalArgumentException("Negative error");
        }

        if (userFields.year < 0) {
            throw new IllegalArgumentException("Negative error");
        }

        if (userFields.month < 0) {
            throw new IllegalArgumentException("Negative error");
        }

        if (userFields.day < 0) {
            throw new IllegalArgumentException("Negative error");
        }

    }


    @Getter
    private static class UserFields {
        private final int age;
        private final int year;
        private final int month;
        private final int day;

        public UserFields(int age, int year, int month, int day) {
            this.age = age;
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
}
