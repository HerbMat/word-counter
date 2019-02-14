package com.solid.wordcounter.handlers;

import com.solid.wordcounter.exception.GenericServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * General exception handler for all word validation errors.
 *
 * @author Mateusz Kozłowski <matikz1110@gmail.com>
 */
@ControllerAdvice
@RestController
@AllArgsConstructor
@Log4j2
public class RestExceptionHandler {

    private static final String UNEXPECTED_ERROR_MESSAGE_CD = "unexpected.processing.error";

    private MessageSource messageSource;

    /**
     * It handles all errors with bad user input.
     *
     * @param ex handled exception
     *
     * @return list of error messages to print in response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("Handling input validation exceptions.", ex);
        return ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }

    /**
     * It handles all errors with validation before save to database.
     *
     * @param ex handled exception
     *
     * @return list of error messages to print in response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public List<String> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        log.debug("Handling database validation exceptions.", ex);

        return ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    /**
     * It handles all unexpected errors on service side.
     *
     * @param ex handled exception
     *
     * @return generic error message
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({GenericServiceException.class})
    public List<String> handleGenericServiceExceptions(GenericServiceException ex, Locale locale) {
        log.error("Handling unexpected error.", ex);

        return List.of(
                messageSource.getMessage(UNEXPECTED_ERROR_MESSAGE_CD, null, locale)
        );
    }
}
