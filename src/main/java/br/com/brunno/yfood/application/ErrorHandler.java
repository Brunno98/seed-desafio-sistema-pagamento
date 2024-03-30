package br.com.brunno.yfood.application;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BindException.class)
    public ProblemDetail handle(BindException e) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more validation error has occurried.");
        problemDetail.setProperty("errors", validationErrorResponse);
        return problemDetail;
    }

    static class ValidationErrorResponse {

        private final List<String> globalErrors;
        private final List<Map<String, String>> fieldErrors;

        public ValidationErrorResponse(BindingResult bindingResult) {
            this.globalErrors = bindingResult.getGlobalErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            this.fieldErrors = bindingResult.getFieldErrors().stream().map(error -> {
                String message = Optional.ofNullable(error.getDefaultMessage()).orElse("Unknown");
                return Map.of("field", error.getField(),
                        "message", message);
            }).collect(Collectors.toList());
        }

        public List<String> getGlobalErrors() {
            return globalErrors;
        }

        public List<Map<String, String>> getFieldErrors() {
            return fieldErrors;
        }
    }
}
