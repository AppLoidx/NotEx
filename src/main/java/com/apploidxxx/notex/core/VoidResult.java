package com.apploidxxx.notex.core;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class VoidResult<S> {

    @Nullable
    private final Notification<S> notification;

    public VoidResult(@Nullable Notification<S> notification) {
        this.notification = notification;
    }

    protected VoidResult() {
        this.notification = null;
    }

    public boolean isOk() {
        return getNotification().isEmpty();
    }

    public <R> R resolve(R onSuccess, R onError) {
        return isOk() ? onSuccess : onError;
    }


    public <R> R resolve(Supplier<R> onSuccess, Supplier<R> onError) {
        return isOk() ? onSuccess.get() : onError.get();
    }

    public void solve(Solvable<S> solvable) {
        if (!isOk() && notification != null) {  // some stupid check
            notification.getErrorObject().ifPresent(solvable::solve);
        }
    }

    @Nullable
    public <R> R solve(R onSuccess, Resolvable<S, R> resolvable) {
        if (!isOk() && notification != null) {  // some stupid check
            // TODO: re-think about nullable contract and interaction
            return notification.getErrorObject().map(resolvable::apply).orElse(null);
        } else {
            return onSuccess;
        }
    }

    /**
     * @return optional notification
     */
    public Optional<Notification<S>> getNotification() {
        return Optional.ofNullable(this.notification);
    }


}
