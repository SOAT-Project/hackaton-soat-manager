package soat.project.hackaton_soat_manager.domain;

import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProcessIdTest {

    @Test
    void givenGeneratedId_whenCreate_thenNotNull() {

        final var id = ProcessId.generate();

        assertNotNull(id.getValue());
    }

    @Test
    void givenValidValue_whenCreate_thenInstantiate() {

        final var id = ProcessId.of("123");

        assertEquals("123", id.getValue());
    }
}