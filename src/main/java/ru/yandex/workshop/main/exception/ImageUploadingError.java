package ru.yandex.workshop.main.exception;

public class ImageUploadingError extends RuntimeException {
    public ImageUploadingError(String message) {
        super(message);
    }
}
