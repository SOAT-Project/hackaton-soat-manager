package soat.project.hackaton_soat_manager.domain.validation;

import java.util.List;
import java.util.Objects;

public interface ValidationHandler {

    ValidationHandler append(DomainError error);
    ValidationHandler append(ValidationHandler handler);

    <T> T validate(Validation<T> validator);

    List<DomainError> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default DomainError firstError() {
        return Objects.nonNull(getErrors()) && !getErrors().isEmpty() ? getErrors().getFirst() : null;
    }

    interface Validation<T> {
        T validate();
    }

}
