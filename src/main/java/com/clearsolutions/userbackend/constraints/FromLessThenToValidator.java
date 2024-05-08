package com.clearsolutions.userbackend.constraints;

import com.clearsolutions.userbackend.api.controller.UserController.FromToDateRequestParameters;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FromLessThenToValidator implements ConstraintValidator<FromLessThenTo, FromToDateRequestParameters> {
	@Override
	public boolean isValid(FromToDateRequestParameters value, ConstraintValidatorContext context) {
		if (value.from() == null || value.to() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("From and to dates are required.").addConstraintViolation();
			return false;
		}
		if (value.from().isAfter(value.to())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					String.format("From (%s) is after to (%s), which is invalid.", value.from(), value.to()))
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}