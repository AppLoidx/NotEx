package com.apploidxxx.notex;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Arthur Kupriyanov
 * @param <T> type for response
 * @param <S> type for notification's error-object
 */
@SuppressWarnings("unused")
@Getter
public class Result<T, S> {

    private final T object;
    @Nullable
    private final Notification<S> notification;

    /**
     *
     * @param object result object (answer)
     * @param notification occurred notification (don't use null here, use {@link Result#Result(Object)} instead)
     */
    public Result(T object, @NotNull Notification<S> notification) {
        this.object = object;
        this.notification = notification;
    }

    /**
     *
     * @param object result object (answer)
     */
    public Result(T object) {
        this.object = object;
        this.notification = null; // NOPMD
    }

    /**
     *
     * @return optional notification
     */
    public Optional<Notification<S>> getNotification() {
        return Optional.ofNullable(this.notification);
    }

    /**
     * It should be used in the end of the "notex" chain
     *
     * @param function action that will be performed
     * @param resolvable functional interface to manage the occurred notification
     * @param <R> result type
     * @return final result
     */
    public <R> R resolve(Function<T, R> function, Resolvable<S, R> resolvable) {
        if (getNotification().isPresent() && getNotification().get().getErrorObject().isPresent()) {
            return resolvable.apply(getNotification().get().getErrorObject().get());
        } else {
            return function.apply(object);
        }
    }

    /**
     * It should be used in the end of the "notex" chain
     *
     * @param resolvable functional interface to manage the occurred notification
     * @return final result
     */
    public T resolve(Resolvable<S, T> resolvable) {
        if (getNotification().isPresent() && getNotification().get().getErrorObject().isPresent()) {
            return resolvable.apply(getNotification().get().getErrorObject().get());
        } else {
            return object;
        }
    }

    /**
     * It should be used in the middle of the "notex" chain
     *
     * @param function action that will be performed
     * @param <R> result type
     * @return intermediate result
     */
    public <R> Result<R, S> apply(Function<T, Result<R, S>> function) {
        return getNotification().<Result<R, S>>map(Result::of)
                .orElseGet(() -> function.apply(this.object));
    }

    /**
     * It should be used in the beginning of the "notex" chain
     * @param function action that will be performed
     * @param <R>result type
     * @return initial result for chain
     */
    public <R> Result<R, S> resolveFrom(Function<T, ? extends R> function) {
        return getNotification().<Result<R, S>>map(Result::of)
                .orElseGet(() -> Result.of(function.apply(this.object)));
    }

    /**
     * Result factory method
     *
     * @param object answer (result) object
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     * @see Result#of(Object, Notification)
     */
    public static <T, S> Result<T, S> of(T object) {
        return new Result<>(object);
    }

    /**
     * Result factory method
     *
     * @param object answer (result) object
     * @param notification occurred notification
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> of(T object, Notification<S> notification) {
        return new Result<>(object, notification);
    }

    /**
     *
     * @param notification occurred notification
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> of(Notification<S> notification) {
        return new Result<>(null, notification);
    }


}
