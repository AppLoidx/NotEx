package compare.nested;

import com.apploidxxx.notex.core.Result;

public class NestedNotex {
    Result<Integer, String> firstMethod() {
        return Result.err();
    }

    Result<Integer, String> wrapper() {
        return firstMethod().apply(val -> Result.ok(),
                val -> Result.err("Error occurred"));
    }

    int main() {
        return wrapper().resolve(-1);
    }
}
