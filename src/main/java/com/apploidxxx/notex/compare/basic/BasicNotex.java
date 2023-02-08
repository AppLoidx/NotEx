package com.apploidxxx.notex.compare.basic;

import com.apploidxxx.notex.core.Result;

public class BasicNotex {
    private Result<Integer, Integer> calc() {
        // some computing code can be there...
        return Result.err();
    }

    private int main() {
        return calc().resolve(0);
    }

}
