package com.apploidxxx.notex;

import com.apploidxxx.notex.core.Notification;
import com.apploidxxx.notex.core.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public final class NotExValidatorUtil {
    private final static ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private final static Validator VALIDATOR = FACTORY.getValidator();

    private NotExValidatorUtil() {}

    public static <T> Result<Boolean, ValidationError<String, String>> validate(T object) {
        Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(object);

        if (violationSet.isEmpty()) {
            return Result.ok(true);
        } else {
            ValidationError<String, String> validationError = new ValidationError<>();
            violationSet.forEach(violation -> {
                validationError.addError(violation.getPropertyPath().toString(), violation.getMessage());
            });

            return Result.of(false, Notification.of(validationError));
        }




    }
}
