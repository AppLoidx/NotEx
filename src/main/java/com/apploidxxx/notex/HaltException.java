package com.apploidxxx.notex;

import java.util.List;

public class HaltException extends RuntimeException {

    protected HaltException() {
        super("Halt exception", new Throwable(), false, false);
    }
}
