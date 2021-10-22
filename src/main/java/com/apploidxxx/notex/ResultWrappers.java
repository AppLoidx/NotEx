package com.apploidxxx.notex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ResultWrappers {

    private static final HaltException haltException = new HaltException();

    private ResultWrappers() {}

    public static <T, S> Stream<T> streamHaltIfError(Stream<Result<T, S>> stream) {
        List<Notification<?>> notifications = new ArrayList<>();
        stream.forEach(r -> r.getNotification().ifPresent(notifications::add));

        if (notifications.isEmpty()) {
            return stream.map(Result::getObject);
        } else {
            throw haltException;
        }
    }
}
