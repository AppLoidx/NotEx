package com.apploidxxx.notex;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ResultWrappers {

    private static final HaltException haltException = new HaltException();

    private static final Consumer<? super Notification<?>> notificationConsumer = (n) -> {
        throw haltException;
    };

    private ResultWrappers() {
    }

    public static <T, S> Stream<T> streamHaltIfError(@NotNull Stream<Result<T, S>> stream) {
        return stream.peek(tsResult -> tsResult
                        .getNotification()
                        .ifPresent(notificationConsumer))
                .map(Result::getObject);
    }
}
