package soat.project.hackaton_soat_manager.domain.video;

import soat.project.hackaton_soat_manager.domain.shared.ValueObject;

import java.util.Set;

public class VideoName extends ValueObject {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("mp4", "avi", "mov", "mkv");

    private final String value;

    private VideoName(String value) {
        this.value = value;
    }

    public static VideoName of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Video name cannot be null or blank");
        }

        String extension = extractExtension(value);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Invalid video extension: " + extension);
        }

        return new VideoName(value);
    }

    private static String extractExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1 || index == fileName.length() - 1) {
            throw new IllegalArgumentException("Video must contain a valid extension");
        }
        return fileName.substring(index + 1);
    }

    public String getValue() {
        return value;
    }
}