package com.example.starter.handler;

import com.example.starter.exception.BadRequestException;
import com.example.starter.exception.BadRequestExceptionDetails;
import com.example.starter.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice //vai dizer para todos os controller qual o tipo de erro chamar
public class RestExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));


        return new ResponseEntity<>(
                ValidationExceptionDetails.builder().
                        timestamp(LocalDateTime.now()).
                        status(HttpStatus.BAD_REQUEST.value()).
                        title("Bad Request Exception, Invalid Fields").
                        details(exception.getMessage()).
                        developerMessage("deu ruim").
                        fields(fields).
                        fieldsMessage(fieldsMessage).
                        build(), HttpStatus.BAD_REQUEST);
    }

}
