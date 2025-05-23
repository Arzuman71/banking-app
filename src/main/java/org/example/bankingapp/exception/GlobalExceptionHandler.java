package org.example.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IncorrectValueException.class, NotEnoughBalanceException.class,
            UserNotFoundException.class, EmailNotFoundException.class,
            PhoneNotFoundException.class, EmailAlreadyExistException.class,
            PhoneAlreadyExistException.class, PasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessExceptions(RuntimeException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }
}
