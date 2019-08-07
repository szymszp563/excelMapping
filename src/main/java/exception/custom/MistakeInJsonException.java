package exception.custom;

public class MistakeInJsonException extends RuntimeException {
    public MistakeInJsonException(String json) {
        super("Mistake in json:\n" + json);
    }
}
