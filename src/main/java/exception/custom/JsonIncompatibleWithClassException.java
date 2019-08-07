package exception.custom;

public class JsonIncompatibleWithClassException extends RuntimeException{
    public JsonIncompatibleWithClassException(String className) {
        super("Json incompatible with: " + className);
    }
}
