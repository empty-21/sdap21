package shinhands.com.exception;

public class ConflictError extends Exception {
    private final int code;

    public ConflictError(int code) {
        super();
        this.code = code;
    }

    public ConflictError(String message) {
        super(message);
        this.code = 100;
    }

    public ConflictError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ConflictError(String message, int code) {
        super(message);
        this.code = code;
    }

    public ConflictError(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
}
