package ru.softplat.security.server.exception;

public class ImagePayloadTooLargeException extends RuntimeException {
    public ImagePayloadTooLargeException(String message) {
        super(message);
    }
}
