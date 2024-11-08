package mariapiabaldoin.progetto_settimana_19.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}