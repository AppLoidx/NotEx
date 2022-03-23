package com.apploidxxx.notex.core;

import org.jetbrains.annotations.Nullable;

/**
 * Functional interface for resolving results with notifications
 *
 * @param <S> type for notification's error-object
 * @param <T> type for response
 */
@FunctionalInterface
public interface Resolvable<S, T> {

    T apply(S errorObject);
}
