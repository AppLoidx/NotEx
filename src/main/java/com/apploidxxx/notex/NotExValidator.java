package com.apploidxxx.notex;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public final class NotExValidator {
    private final static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final static Validator validator = factory.getValidator();

    private NotExValidator() {}

    public static <T> Result<Boolean, ValidationError<String, String>> validate(T object) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(object);

        if (violationSet.isEmpty()) {
            return Result.of(true);
        } else {
            ValidationError<String, String> validationError = new ValidationError<>();
            violationSet.forEach(violation -> {
                validationError.addError(violation.getPropertyPath().toString(), violation.getMessage());
            });

            return Result.of(false, Notification.of(validationError));
        }




    }
}
