package com.apploidxxx.notex.core;

import java.util.function.Function;

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
