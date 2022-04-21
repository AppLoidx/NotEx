package com.apploidxxx.notex;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NotExValidatorUtil {
    private final static ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private final static Validator VALIDATOR = FACTORY.getValidator();

    private NotExValidatorUtil() {}

    private static Boolean onEachError(BiConsumer<String, String> consumer,
                                       Notification<ValidationError<String, String>> errorNotification) {

            errorNotification.getErrorObject()
                    .ifPresent((errObj ->
                            errObj.onEachError(consumer)));

            return false;
    }

    public static <T> boolean validate(T object, BiConsumer<String, String> consumer) {
        Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(object);

        if (violationSet.isEmpty()) {
            return true;
        } else {
            ValidationError<String, String> validationError = new ValidationError<>();
            violationSet.forEach(violation -> {
                validationError.addError(violation.getPropertyPath().toString(), violation.getMessage());
            });

            return Result.of(false, Notification.of(validationError))
                    .resolve(en -> onEachError(consumer, en));
        }




    }
}
