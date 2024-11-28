package fr.diginamic.hello.exception;

public class FunctionalException extends Exception {
    public FunctionalException(String message) {
        super("F-Test : " +message);
    }
}
