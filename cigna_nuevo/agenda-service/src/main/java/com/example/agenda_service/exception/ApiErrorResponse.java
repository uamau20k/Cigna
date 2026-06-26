package com.example.agenda_service.exception;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data @Builder
public class ApiErrorResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
