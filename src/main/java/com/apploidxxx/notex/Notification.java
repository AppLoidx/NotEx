package com.apploidxxx.notex;

import lombok.RequiredArgsConstructor;

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
     *
     * @return optional error-object
     */
    public Optional<T> getErrorObject() {
        return Optional.ofNullable(errorObject);
    }



}
