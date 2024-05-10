package com.clearsolutions.userbackend.constraints;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {
	@Override
	public boolean isValid(final LocalDate valueToValidate, final ConstraintValidatorContext context) {
		LocalDate curDate = LocalDate.now();
		if (valueToValidate != null) {
			return Period.between(valueToValidate, curDate).getYears() >= 18;
		} else
			return false;
	}
}