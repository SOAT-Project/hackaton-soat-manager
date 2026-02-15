package soat.project.hackaton_soat_manager.domain.shared;

import java.time.Instant;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(
            final ID id,
            final Instant createdAt
    ) {
        super(id, createdAt);
    }
}