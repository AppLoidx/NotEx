package compare.rethrow;

public class RethrowException {

    void helper() {
        throw new ArithmeticException();
    }

    void main() {
        try {
            helper();
        } catch (ArithmeticException exception) {
            System.out.println("Exception occurred"); // log
            throw new ArithmeticException(); // rethrow
        }
    }
}
