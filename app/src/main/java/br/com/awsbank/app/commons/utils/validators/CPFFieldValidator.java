package br.com.awsbank.app.commons.utils.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;

public class CPFFieldValidator {

    private static final Validator FIELD_VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        FIELD_VALIDATOR = factory.getValidator();
    }

    public static boolean isValid(String cpf) throws IllegalArgumentException {
        CPFWrapper wrapper = new CPFWrapper(cpf);
        Set<ConstraintViolation<CPFWrapper>> violations = FIELD_VALIDATOR.validate(wrapper);

        return violations.isEmpty();
    }

    private static class CPFWrapper {
        @CPF
        private final String cpf;

        public CPFWrapper(String cpf) {
            this.cpf = cpf;
        }
    }
}
