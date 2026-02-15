package soat.project.hackaton_soat_manager.domain.video;

import soat.project.hackaton_soat_manager.domain.shared.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ProcessId extends Identifier {

    private final String value;

    private ProcessId(String value) {
        this.value = value;
    }

    public static ProcessId generate() {
        return new ProcessId(UUID.randomUUID().toString());
    }

    public static ProcessId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProcessId cannot be null or blank");
        }
        return new ProcessId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessId that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}