package shinhands.com.exception;

public class DefaultException extends Exception {
    // private static final long serialVersionUID = 7718828512143293558 L;
    private final int code;

    public DefaultException(int code) {
        super();
        this.code = code;
    }

    public DefaultException(String message) {
        super(message);
        this.code = 100;
    }

    public DefaultException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public DefaultException(String message, int code) {
        super(message);
        this.code = code;
    }

    public DefaultException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
}
