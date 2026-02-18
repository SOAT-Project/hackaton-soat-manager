package soat.project.hackaton_soat_manager.domain.exception;


import soat.project.hackaton_soat_manager.domain.validation.DomainError;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<DomainError> errors;

    protected DomainException(final String aMessage, final List<DomainError> someErrors) {
        super(aMessage);
        this.errors = someErrors;
    }

    public static DomainException with(final List<DomainError> someErrors) {
        return new DomainException("", someErrors);
    }

    public static DomainException with(final DomainError anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public List<DomainError> getErrors() {
        return errors;
    }

}
