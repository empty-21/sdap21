package shinhands.com.exception;

public class ForbiddenError extends Exception {
    private final int code;

    public ForbiddenError(int code) {
        super();
        this.code = code;
    }

    public ForbiddenError(String message) {
        super(message);
        this.code = 100;
    }

    public ForbiddenError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ForbiddenError(String message, int code) {
        super(message);
        this.code = code;
    }

    public ForbiddenError(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
