package tn.esprit.springfever.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {


    public static class ValidationErrorResponse {
        private Map<String, String> errors = new HashMap<>();

        public void addError(String field, String message) {
            errors.put(field, message);
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ValidationErrorResponse response = new ValidationErrorResponse();
        for (FieldError fieldError : fieldErrors) {
            response.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }
}
