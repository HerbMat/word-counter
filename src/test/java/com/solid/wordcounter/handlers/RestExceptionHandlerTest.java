package com.solid.wordcounter.handlers;

import com.solid.wordcounter.exception.GenericServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exceptionMock = createMethodArgumentNotValidExceptionMock();

        List<String> result = restExceptionHandler.handleMethodArgumentNotValidException(exceptionMock);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(2));
        assertThat(result, hasItems("error1", "error2"));
    }

    @Test
    public void handleConstraintViolationExceptions() {
        ConstraintViolationException exceptionMock = createConstraintViolationExceptionMock();

        List<String> result = restExceptionHandler.handleConstraintViolationExceptions(exceptionMock);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(2));
        assertThat(result, hasItems("error1", "error2"));
    }

    @Test
    public void handleGenericServiceExceptions() {
        GenericServiceException genericServiceException = mock(GenericServiceException.class);

        when(messageSource.getMessage(anyString(), eq(null), any(Locale.class))).thenReturn("error");

        List<String> result = restExceptionHandler
                .handleGenericServiceExceptions(genericServiceException, Locale.ENGLISH);

        assertThat(result, notNullValue());
        assertThat(result, hasSize(1));
        assertThat(result, hasItems("error"));

    }

    private MethodArgumentNotValidException createMethodArgumentNotValidExceptionMock() {
        MethodArgumentNotValidException exceptionMock = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResultMock = mock(BindingResult.class);
        ObjectError objectError1 = mock(ObjectError.class);
        ObjectError objectError2 = mock(ObjectError.class);

        when(exceptionMock.getBindingResult()).thenReturn(bindingResultMock);
        when(bindingResultMock.getAllErrors())
                .thenReturn(List.of(objectError1, objectError2));
        when(objectError1.getDefaultMessage()).thenReturn("error1");
        when(objectError2.getDefaultMessage()).thenReturn("error2");

        return exceptionMock;
    }

    private ConstraintViolationException createConstraintViolationExceptionMock() {
        ConstraintViolationException exceptionMock = mock(ConstraintViolationException.class);

        ConstraintViolation<String> constraintViolationMock1 = mock(ConstraintViolation.class);
        ConstraintViolation<String> constraintViolationMock2 = mock(ConstraintViolation.class);

        when(constraintViolationMock1.getMessage()).thenReturn("error1");
        when(constraintViolationMock2.getMessage()).thenReturn("error2");
        when(exceptionMock.getConstraintViolations())
                .thenReturn(Set.of(constraintViolationMock1, constraintViolationMock2));

        return exceptionMock;
    }
}