import com.apploidxxx.notex.NotExValidator;
import com.apploidxxx.notex.Result;
import com.apploidxxx.notex.ValidationError;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.junit.Test;


public class ValidatorExample {

    @Test
    public void validatorExample() {
        User user = new User(5, null);

        Result<Boolean, ValidationError<String, String>> validate = NotExValidator.validate(user);

        Boolean resolve = validate.resolve(validationError -> {
            validationError.onEachError((field, msg) -> System.out.printf("Error on field %s : %s%n", field, msg));
            return false;
        });

        System.out.println("No errors : " + resolve);


    }

    @Getter
    private static class User {
        @Min(10)
        private final int age;
        @NotNull
        private final String name;


        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }
}
