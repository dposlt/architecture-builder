package dvoraka.archbuilder.exception;

public class GeneratorException extends RuntimeException {

    private static final long serialVersionUID = -7453870399398662172L;


    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(Throwable cause) {
        super(cause);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
