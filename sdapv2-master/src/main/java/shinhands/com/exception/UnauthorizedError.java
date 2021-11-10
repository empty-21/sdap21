package shinhands.com.exception;

public class UnauthorizedError extends Exception {
    private final int code;

    public UnauthorizedError(int code) {
        super();
        this.code = code;
    }

    public UnauthorizedError(String message) {
        super(message);
        this.code = 100;
    }

    public UnauthorizedError(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public UnauthorizedError(String message, int code) {
        super(message);
        this.code = code;
    }

    public UnauthorizedError(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public int getCode() {
        return this.code;
    }
}
