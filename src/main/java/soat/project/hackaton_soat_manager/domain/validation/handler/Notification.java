package soat.project.hackaton_soat_manager.domain.validation.handler;

import soat.project.hackaton_soat_manager.domain.exception.DomainException;
import soat.project.hackaton_soat_manager.domain.validation.DomainError;
import soat.project.hackaton_soat_manager.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<DomainError> errors;

    private Notification(final List<DomainError> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final DomainError error) {
        return new Notification(new ArrayList<>()).append(error);
    }


    @Override
    public Notification append(final DomainError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(Validation<T> validator) {
        try {
            return validator.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new DomainError(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<DomainError> getErrors() {
        return this.errors;
    }

}
