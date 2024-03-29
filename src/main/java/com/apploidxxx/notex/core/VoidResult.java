package com.apploidxxx.notex.core;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class VoidResult<S> {

    @Nullable
    private final Notification<S> notification;

    public VoidResult(@Nullable Notification<S> notification) {
        this.notification = notification;
    }

    protected VoidResult() {
        this(null);
    }

    public boolean isOk() {
        return getNotification().isEmpty();
    }

    public <R> R resolve(R onSuccess, R onError, Runnable finalizer) {
        return resolve(() -> onSuccess, () -> onError, finalizer);
    }

    public <R> R resolve(R onSuccess, R onError) {
        return resolve(() -> onSuccess, () -> onError, () -> {});
    }


    public <R> R resolve(Supplier<R> onSuccess, Supplier<R> onError, Runnable finalizer) {
        try {
            return isOk() ? onSuccess.get() : onError.get();
        } finally {
            finalizer.run();
        }
    }

    public void solve(Solvable<S> solvable) {
        if (!isOk() && notification != null) {  // some stupid check
            notification.getErrorObject().ifPresent(solvable::solve);
        }
    }

    @Nullable
    public <R> R solve(R onSuccess, Resolvable<S, R> resolvable) {
        if (isOk() || notification == null) { // some stupid check
            return onSuccess;
        } else {
            // TODO: re-think about nullable contract and interaction
            return notification.getErrorObject().map(resolvable::apply).orElse(null);
        }
    }

    public VoidResult<S> lookup(Solvable<S> lookup) {
        lookup(lookup, () -> {});
        return this;
    }

    public VoidResult<S> lookup(Solvable<S> lookup, Runnable onNoError) {

        getNotification()
                .flatMap(Notification::getErrorObject)
                .ifPresentOrElse(lookup::solve, onNoError);
        return this;
    }

    /**
     * @return optional notification
     */
    public Optional<Notification<S>> getNotification() {
        return Optional.ofNullable(this.notification);
    }


}
