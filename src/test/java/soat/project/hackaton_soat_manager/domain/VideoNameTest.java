package soat.project.hackaton_soat_manager.domain;

import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.domain.video.VideoName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VideoNameTest {

    @Test
    void givenValidName_whenCreate_thenInstantiate() {

        final var name = VideoName.of("video.mp4");

        assertEquals("video.mp4", name.getValue());
    }

    @Test
    void givenBlankName_whenCreate_thenThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> VideoName.of("")
        );
    }

    @Test
    void givenNullName_whenCreate_thenThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> VideoName.of(null)
        );
    }
}