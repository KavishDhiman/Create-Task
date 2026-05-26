package com.createtask.createtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Centralized handler — catches exceptions from all controllers and returns clean JSON errors
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildError(HttpStatus status, String message) {

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return body;
    }

    // 404 - generic resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - role not found
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotFound(RoleNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - project not found
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjectNotFound(ProjectNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 409 - generic duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - duplicate user
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUser(DuplicateUserException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - duplicate role
    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateRole(DuplicateRoleException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - duplicate project
    @ExceptionHandler(DuplicateProjectException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateProject(DuplicateProjectException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - Cannot delete user because role mappings exist
    @ExceptionHandler(UserRoleMappingExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserRoleMappingExists(
            UserRoleMappingExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - role already assigned
    @ExceptionHandler(RoleAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> handleRoleAlreadyAssigned(RoleAlreadyAssignedException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 400 - validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        String validationMessage = "Validation failed";

        FieldError fieldError =
                ex.getBindingResult().getFieldError();

        if(fieldError != null){
            validationMessage = fieldError.getDefaultMessage();
        }

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", validationMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 400 - validation errors from @Positive on path variables
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex) {

        String message = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(v -> {
                    String field = v.getPropertyPath().toString();
                    String param = field.contains(".")
                            ? field.substring(field.lastIndexOf('.') + 1)
                            : field;
                    return "Invalid value for '" + param + "': " + v.getMessage();
                })
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }

    // 500 - catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unexpected error: " + ex.getMessage()
                ));
    }

    // 404 - attachment not found
    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAttachmentNotFound(
            AttachmentNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - comment not found
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCommentNotFound(
            CommentNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(NotificationRecipientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationRecipientNotFound(
            NotificationRecipientNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    // 404 - notification not found by ID
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationNotFound(NotificationNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage())); // Thrown when GET/DELETE uses a missing ID
    }

    @ExceptionHandler(NotificationAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationAlreadyExists(
            NotificationAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        String msg = ex.getReason() != null ? ex.getReason() : "Invalid data";

        return ResponseEntity.status(status)
                .body(buildError(status, msg));
    }
    // Handles invalid datatype inputs — includes parameter name for clearer error message
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("error", "Invalid Input");
        error.put("message", "Invalid value for parameter '" + ex.getName() + "': expected a numeric value.");
        error.put("status", 400);
        error.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handles invalid JSON datatype values
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(
            HttpMessageNotReadableException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("error", "Invalid Input");
        error.put("message", "Please enter valid numeric values for numeric fields.");
        error.put("status", 400);
        error.put("timestamp", LocalDateTime.now().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handles invalid or non-existing API endpoint requests and returns HTTP 404 response
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(
            NoResourceFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(
                        HttpStatus.NOT_FOUND,
                        "Invalid API endpoint: " + ex.getResourcePath()
                ));
    }

    // ── Task & Category module handlers (added by Jayanthi) ──────────────────

    // 404 - task not found by ID
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(
            TaskNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - category not found by ID
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(
            CategoryNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 404 - task-category mapping not found
    @ExceptionHandler(TaskCategoryMappingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskCategoryMappingNotFound(
            TaskCategoryMappingNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 409 - duplicate category name or ID
    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateCategory(
            DuplicateCategoryException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 409 - category already assigned to this task
    @ExceptionHandler(TaskCategoryAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> handleTaskCategoryAlreadyAssigned(
            TaskCategoryAlreadyAssignedException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 400 - required @RequestParam missing from URL (e.g. ?days= omitted)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(
            MissingServletRequestParameterException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(
                        HttpStatus.BAD_REQUEST,
                        "Required request parameter '" + ex.getParameterName() + "' is missing."
                ));
    }

}