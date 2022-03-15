package com.apploidxxx.notex.core;

import java.util.function.Supplier;

public abstract class VoidResult {

    public abstract boolean isOk();

    public <R> R resolve(R onSuccess, R onError) {
        return isOk() ? onSuccess : onError;
    }

    public <R> R resolve(Supplier<R> onSuccess, Supplier<R> onError) {
        return isOk() ? onSuccess.get() : onError.get();
    }
}
