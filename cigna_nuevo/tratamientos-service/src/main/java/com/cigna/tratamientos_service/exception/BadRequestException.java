package com.example.tratamientos_service.exception;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
