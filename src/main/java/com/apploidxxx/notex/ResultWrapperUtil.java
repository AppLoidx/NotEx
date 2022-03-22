package com.apploidxxx.notex;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import com.apploidxxx.notex.exception.HaltException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ResultWrapperUtil {

    private static final HaltException HALT_EXCEPTION = new HaltException();

    private static final Consumer<? super Notification<?>> NOTIFICATION_CONSUMER = (n) -> {
        throw HALT_EXCEPTION;
    };

    private ResultWrapperUtil() {
    }

    public static <T, S> Stream<T> streamHaltIfError(@NotNull Stream<Result<T, S>> stream) {
        return stream.peek(tsResult -> tsResult
                        .getNotification()
                        .ifPresent(NOTIFICATION_CONSUMER))
                .map(Result::getObject);
    }
}
