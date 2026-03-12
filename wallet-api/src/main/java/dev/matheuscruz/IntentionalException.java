package dev.matheuscruz;

// Ela é uma IntentionalException
// Ela é uma RuntimeException
// Ela é uma unchecked exception
// Ela é uma custom exception
public class IntentionalException extends RuntimeException {
    public IntentionalException(String message, Exception e) {
        super(message, e);
    }
}
