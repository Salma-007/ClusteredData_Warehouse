package com.example.datawarehouse.exception;

import com.example.datawarehouse.dto.ErrorDetail;
import com.example.datawarehouse.dto.ImportSummaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ImportSummaryResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    // We cannot extract the ID easily here as the list is rejected as a whole.
                    // A better approach is to rely on the service to handle individual deal errors.
                    return new ErrorDetail(fieldName, errorMessage);
                })
                .collect(Collectors.toList());

        // For simplicity, we'll reject the entire request if structural validation fails at the top level
        return new ImportSummaryResponse(
                0,
                ex.getBindingResult().getErrorCount(),
                errors
        );
    }
}
