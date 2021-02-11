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
    private final String message;
    private final T errorObject;

    /**
     * Factory method
     *
     * @param message error message
     * @param <T> type of object within {@link Notification}
     * @return {@link Notification} instance
     * @see Notification#of(String, Object)
     */
    public static <T> Notification<T> of(String message) {
        return of(message, null);
    }

    /**
     * Factory method
     *
     * @param message error message
     * @param object object within {@link Notification}
     * @param <T> type of object within {@link Notification}
     * @return {@link Notification} instance
     */
    public static <T> Notification<T> of(String message, T object) {
        return new Notification<>(message, object);
    }

    /**
     *
     * @return optional error-object
     */
    public Optional<T> getErrorObject() {
        return Optional.ofNullable(errorObject);
    }

    /**
     *
     * @return message of error
     */
    public String getMessage() {
        return message;
    }

}
