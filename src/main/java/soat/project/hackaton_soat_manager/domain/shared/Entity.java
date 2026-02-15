package soat.project.hackaton_soat_manager.domain.shared;

import soat.project.hackaton_soat_manager.domain.validation.ValidationHandler;

import java.time.Instant;

public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    protected final Instant createdAt;

    protected Entity(
            final ID id,
            final Instant createdAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

