package com.apploidxxx.notex;

import java.util.function.Function;

/**
 * Functional interface for resolving results with notifications
 *
 * @param <S> type for notification's error-object
 * @param <T> type for response
 */
@FunctionalInterface
public interface Resolvable<S, T> {
    /**
     *
     * @param notification from resolve (occurred error)
     * @return default (error, empty) value for next resolve statement
     * @see Result#resolve(Resolvable)
     */
    T apply(Notification<S> notification);
}
