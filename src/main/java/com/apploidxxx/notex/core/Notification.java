package com.apploidxxx.notex.core;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Arthur Kupriyanov
 * @param <T> type for response
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class Notification<T> {
    private final T errorObject;


    /**
     * Factory method
     *
     * @param object object within {@link Notification}
     * @param <T> type of object within {@link Notification}
     * @return {@link Notification} instance
     */
    public static <T> Notification<T> of(T object) {
        return new Notification<>(object);
    }

    /**
     * Factory method
     *
     * @param <T> type of object within {@link Notification}
     * @return {@link Notification} instance
     */
    public static <T> Notification<T> empty() {
        return new Notification<>(null);
    }

    /**
     *
     * @return optional error-object
     */
    @NotNull
    public Optional<T> getErrorObject() {
        return Optional.ofNullable(errorObject);
    }



}
