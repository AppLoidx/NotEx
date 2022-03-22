package com.apploidxxx.notex.core;

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
@SuppressWarnings({"unused", "PMD.TooManyMethods"})
@Getter
public class Result<T, S> extends VoidResult {

    private final T object;
    @Nullable
    private final Notification<S> notification;

    /**
     *
     * @param object result object (answer)
     * @param notification occurred notification (don't use null here, use {@link Result#Result(Object)} instead)
     */
    public Result(T object, @NotNull Notification<S> notification) {
        super();
        this.object = object;
        this.notification = notification;
    }

    /**
     *
     * @param object result object (answer)
     */
    public Result(T object) {
        super();
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
    public <R> R resolve(Function<T, R> function, Resolvable<Notification<S>, R> resolvable) {
        return errorObject()
                .map(resolvable::apply)
                .orElseGet(() -> function.apply(object));
    }

    @Override
    public <R> R resolve(R onSuccess, R onError) {
        return isOk() ? onSuccess : onError;
    }

    public T resolve(T onError) {
        return resolve(object, onError);
    }

    /**
     * It should be used in the end of the "notex" chain
     *
     * @param resolvable functional interface to manage the occurred notification
     * @return final result
     */
    public T resolve(Resolvable<Notification<S>, T> resolvable) {
        return resolve(Function.identity(), resolvable);
    }

    private Optional<Notification<S>> errorObject() {
        return Optional.ofNullable(notification);
    }

    /**
     * It should be used in the middle of the "notex" chain
     *
     * @param function action that will be performed
     * @param <R> result type
     * @return intermediate result
     */
    public <R> Result<R, S> apply(Function<T, Result<R, S>> function) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> function.apply(this.object));
    }

    /**
     * It should be used in the middle of the "notex" chain
     *
     * @param function action that will be performed
     * @param errFunction error handler
     * @return intermediate result
     */
    public <R> Result<R, S> apply(Function<T, Result<R, S>> function, Function<Notification<S>, Result<R, S>> errFunction) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> function.apply(this.object).resolve(Result::ok, errFunction::apply));
    }
    /**
     * It should be used in the beginning of the "notex" chain
     * @param function action that will be performed
     * @param <R>result type
     * @return initial result for chain
     */
    public <R> Result<R, S> resolveFrom(Function<T, ? extends R> function) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> Result.ok(function.apply(this.object)));
    }

    @Override
    public boolean isOk() {
        return notification == null;
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
    public static <T, S> Result<T, S> ok(T object) {
        return new Result<>(object);
    }

    /**
     * Result factory method
     *
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     * @see Result#of(Object, Notification)
     */
    public static <T, S> Result<T, S> ok() {
        return new Result<>(null);
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
    public static <T, S> Result<T, S> err(Notification<S> notification) {
        return new Result<>(null, notification);
    }

    /**
     *
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> err() {
        return new Result<>(null, Notification.empty());
    }


}
