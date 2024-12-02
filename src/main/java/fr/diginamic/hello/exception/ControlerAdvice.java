package fr.diginamic.hello.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public abstract class ControlerAdvice {

    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<String> traiterFunctionalException(FunctionalException ex) {
        return ResponseEntity.badRequest().body("* " + ex.getMessage());
    }
}
