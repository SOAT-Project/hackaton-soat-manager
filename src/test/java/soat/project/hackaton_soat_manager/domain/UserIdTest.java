package soat.project.hackaton_soat_manager.domain;

import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.domain.video.UserId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserIdTest {

    @Test
    void givenValidUserId_whenCreate_thenInstantiate() {

        final var id = UserId.of("user123");

        assertEquals("user123", id.getValue());
    }

    @Test
    void givenBlankUserId_whenCreate_thenThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> UserId.of("")
        );
    }
}