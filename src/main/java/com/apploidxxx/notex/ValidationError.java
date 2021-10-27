package com.apploidxxx.notex;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ValidationError<T, S> {
    private final Map<T, S> errorMap = new HashMap<>();
    private boolean hasError;

    public void addError(T field, S value) {
        hasError = true;
        errorMap.put(field, value);
    }

    public boolean isHasError() {
        return hasError;
    }

    public void onEachError(BiConsumer<T, S> consumer) {
        errorMap.forEach(consumer);
    }
}
