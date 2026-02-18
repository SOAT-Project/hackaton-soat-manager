package soat.project.hackaton_soat_manager.domain.validation.handler;

import soat.project.hackaton_soat_manager.domain.exception.DomainException;
import soat.project.hackaton_soat_manager.domain.validation.DomainError;
import soat.project.hackaton_soat_manager.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final DomainError anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception e) {
            throw DomainException.with(new DomainError(e.getMessage()));
        }
    }

    @Override
    public List<DomainError> getErrors() {
        return null;
    }

}

