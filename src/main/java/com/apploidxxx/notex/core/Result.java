package com.apploidxxx.notex.core;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * @param <T> type for response
 * @param <S> type for notification's error-object
 * @author Arthur Kupriyanov
 */
@SuppressWarnings({"unused", "PMD.TooManyMethods"})
@Getter
public class Result<T, S> extends VoidResult<S> {

    private final T object;


    /**
     * @param object       result object (answer)
     * @param notification occurred notification (don't use null here, use {@link Result#Result(Object)} instead)
     */
    public Result(T object, @NotNull Notification<S> notification) {
        super(notification);
        this.object = object;
    }

    /**
     * @param object result object (answer)
     */
    public Result(T object) {
        super();
        this.object = object;
    }


    /**
     * It should be used in the end of the "notex" chain
     *
     * @param function   action that will be performed
     * @param resolvable functional interface to manage the occurred notification
     * @param <R>        result type
     * @return final result
     */
    public <R> R resolve(Function<T, R> function, Resolvable<Notification<S>, R> resolvable, Runnable finalizer) {
        try {
            return getNotification()
                    .map(resolvable::apply)
                    .orElseGet(() -> function.apply(object));
        } finally {
            finalizer.run();
        }

    }

    /**
     * It should be used in the end of the "notex" chain
     *
     * @param function   action that will be performed
     * @param resolvable functional interface to manage the occurred notification
     * @param <R>        result type
     * @return final result
     */
    public <R> R resolve(Function<T, R> function, Resolvable<Notification<S>, R> resolvable) {
        return resolve(function, resolvable, () -> {});
    }


    public T resolve(T onError, Runnable finalizer) {
        return resolve(object, onError, finalizer);
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

    public T resolve(Resolvable<Notification<S>, T> resolvable, Runnable runnable) {
        return resolve(Function.identity(), resolvable, runnable);
    }

    public Result<T, S> resolveFor(Class<? extends S> clazz, T onError) {
        return resolveFor(clazz, (err) -> onError, () -> {
        });

    }

    public Result<T, S> resolveFor(Class<? extends S> clazz, T onError, Runnable finalizer) {
        return resolveFor(clazz, (err) -> onError, finalizer);

    }

    public <X> Result<T, S> resolveFor(Class<? extends S> clazz, Resolvable<Notification<S>, T> resolvable, Runnable finalizer) {
        Optional<Notification<S>> sNotification = getNotification();
        if (isOk() || sNotification.isEmpty()) {
            return this;
        }
        Optional<S> errorObject = sNotification.get().getErrorObject();


        if (errorObject.isPresent() && clazz.isAssignableFrom(errorObject.get().getClass())) {
            return Result.ok(resolve(resolvable));
        } else {
            return this;
        }


    }


    /**
     * It should be used in the middle of the "notex" chain
     *
     * @param function action that will be performed
     * @param <R>      result type
     * @return intermediate result
     */
    public <R> Result<R, S> apply(Function<T, Result<R, S>> function) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> function.apply(this.object));
    }

    /**
     * It should be used in the middle of the "notex" chain
     *
     * @param function    action that will be performed
     * @param errFunction error handler
     * @return intermediate result
     */
    public <R> Result<R, S> apply(Function<T, Result<R, S>> function, Function<Notification<S>, Result<R, S>> errFunction) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> function.apply(this.object).resolve(Result::ok, errFunction::apply));
    }

    /**
     * It should be used in the beginning of the "notex" chain
     *
     * @param function  action that will be performed
     * @param <R>result type
     * @return initial result for chain
     */
    public <R> Result<R, S> resolveFrom(Function<T, ? extends R> function) {
        return getNotification().<Result<R, S>>map(Result::err)
                .orElseGet(() -> Result.ok(function.apply(this.object)));
    }

    public Result<T, S> lookup(Solvable<S> lookup) {
        lookup(lookup, () -> {
        });
        return this;
    }

    public Result<T, S> lookup(Solvable<S> lookup, Runnable onNoError) {
        super.lookup(lookup, onNoError);
        return this;
    }

    /**
     * Result factory method
     *
     * @param object answer (result) object
     * @param <T>    answer (result) type
     * @param <S>    notification error-object type
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
     * @param object       answer (result) object
     * @param notification occurred notification
     * @param <T>          answer (result) type
     * @param <S>          notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> of(T object, Notification<S> notification) {
        return new Result<>(object, notification);
    }

    /**
     * @param notification occurred notification
     * @param <T>          answer (result) type
     * @param <S>          notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> err(Notification<S> notification) {
        return new Result<>(null, notification);
    }

    public static <T, S> Result<T, S> err(S errorObject) {
        return err(Notification.of(errorObject));
    }

    /**
     * @param <T> answer (result) type
     * @param <S> notification error-object type
     * @return instance of {@link Result}
     */
    public static <T, S> Result<T, S> err() {
        return new Result<>(null, Notification.empty());
    }


}
