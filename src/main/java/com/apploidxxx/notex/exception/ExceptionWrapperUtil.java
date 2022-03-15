package com.apploidxxx.notex.exception;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ExceptionWrapperUtil {

    public static <T, S> Result<T, S> wrapException(Supplier<T> supplier, Function<RuntimeException, S> onError) {
        try {
            return Result.ok(supplier.get());
        } catch (RuntimeException e) {
            return Result.err(Notification.of(onError.apply(e)));
        }
    }

    public static <T> Result<T, Exception> wrapException(Supplier<T> supplier) {
        return wrapException(supplier, e -> e);
    }

}
