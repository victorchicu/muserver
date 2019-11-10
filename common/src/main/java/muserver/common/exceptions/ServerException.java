package muserver.common.exceptions;

public class ServerException extends Exception {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }

    public ServerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }
}
