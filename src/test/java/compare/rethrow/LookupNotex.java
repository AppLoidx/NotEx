package compare.rethrow;

import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.core.VoidResult;

public class LookupNotex {

    VoidResult<String> helper() {
        return Result.err("Arithmetic error");
    }
    VoidResult<String> main() {
        return helper().lookup(err -> System.out.println(err));
    }

}
