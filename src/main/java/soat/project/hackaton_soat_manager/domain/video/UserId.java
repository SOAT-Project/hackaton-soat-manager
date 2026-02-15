package soat.project.hackaton_soat_manager.domain.video;

import soat.project.hackaton_soat_manager.domain.shared.Identifier;

import java.util.Objects;

public class UserId extends Identifier {

    private final String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
        return new UserId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}