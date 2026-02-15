package soat.project.hackaton_soat_manager.domain.exception;


import soat.project.hackaton_soat_manager.domain.shared.Entity;
import soat.project.hackaton_soat_manager.domain.validation.DomainError;

import java.util.Collections;
import java.util.List;

public class ConflictException extends DomainException{

    protected ConflictException(final String aMessage, final List<DomainError> someErrors) {
        super(aMessage, someErrors);
    }

    public static ConflictException with(
            final Class<? extends Entity<?>> entity,
            final String id
    ) {
        final var anError = "%s with id %s already exist".formatted(
                entity.getSimpleName().toLowerCase(),
                id
        );
        return new ConflictException(anError, Collections.emptyList());
    }

    public static ConflictException with(final DomainError anError) {
        return new ConflictException(anError.message(), List.of(anError));
    }
}
