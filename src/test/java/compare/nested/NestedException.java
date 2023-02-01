package compare.nested;

public class NestedException {
    int firstMethod() {
        throw new ArithmeticException();
    }

    int wrapper() {
        try {
            return firstMethod();
        } catch (ArithmeticException exception) {
            throw new RuntimeException("exception occurred");
        }
    }

    int main() {
        try {
            return wrapper();
        } catch (ArithmeticException exception) {
            return -1;
        }
    }

}
