package com.carolinawings.webapp.exceptions;


import com.carolinawings.webapp.model.Restaurant;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends  RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName, Long fieldId) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName, String field, UUID id)
    {
        super(String.format("%s not found with %s: %s", resourceName, field, id.toString()));
    }

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String resourceName, String field, Integer id) {
        super(String.format("%s not found with %s %d", resourceName, field, id));
    }

    public ResourceNotFoundException(String user, String email, String username) {
        super(String.format("%s not found with %s %s", user, email, username));
    }
}
