package br.com.awsbank.app.authentication.domain.models.credential;

import br.com.awsbank.app.authentication.domain.exceptions.ValidationException;
import lombok.Getter;

@Getter
public enum CredentialType {
    PASSWORD("PASSWORD") {
        @Override
        public ValidationException.ValidationExceptionDomainModel validate(String value, String cpf) throws IllegalArgumentException {
            boolean isValidPassword =
                    value != null && value.matches("^\\d{8}$") && isValid(value, cpf);

            if (!isValidPassword) {
                return new ValidationException.ValidationExceptionDomainModel("Password must be exactly 8 digits and cannot contain sequential numbers or the user's CPF.", "PASSWORD");
            }
            return null;
        }
    },
    TRANSACTION_PASSWORD("TRANSACTION_PASSWORD") {
        @Override
        public ValidationException.ValidationExceptionDomainModel validate(String value, String cpf) throws IllegalArgumentException {
            boolean isValidPassword =
                    value != null && value.matches("^\\d{6}$") && isValid(value, cpf);

            if (!isValidPassword) {
                return new ValidationException.ValidationExceptionDomainModel("Password must be exactly 6 digits and cannot contain sequential numbers or the user's CPF.", "TRANSACTION_PASSWORD");
            }
            return null;
        }
    };

    private final String type;

    public abstract ValidationException.ValidationExceptionDomainModel validate(String value, String cpf) throws IllegalArgumentException;

    CredentialType(String type) {
        this.type = type;
    }

    private static boolean isValid(String value, String cpf) {
        if (cpf != null && !cpf.isEmpty() && value.contains(cpf)) {
            return false;
        }

        return !isSequential(value);
    }

    private static boolean isSequential(String value) {
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < value.length(); i++) {
            if (value.charAt(i) != value.charAt(i - 1) + 1) {
                isIncreasing = false;
            }
            if (value.charAt(i) != value.charAt(i - 1) - 1) {
                isDecreasing = false;
            }
        }
        return isIncreasing || isDecreasing;
    }
}
