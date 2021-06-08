package com.example.starter.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends  ExceptionDetails {
    private String fields;
    private String fieldsMessage;
}
