package ru.yandex.workshop.main.exception;

public class ImagePayloadTooLargeException extends RuntimeException {
    public ImagePayloadTooLargeException(String message) {
        super(message);
    }
}
