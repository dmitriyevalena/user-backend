package com.clearsolutions.userbackend.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FromLessThenToValidator.class)
public @interface FromLessThenTo {
    String message() default "`from` should be more recent then `to`";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}