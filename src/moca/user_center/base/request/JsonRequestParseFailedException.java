package moca.user_center.base.request;

public class JsonRequestParseFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public JsonRequestParseFailedException() {
        super();
    }

    public JsonRequestParseFailedException(String message) {
        super(message);
    }

    public JsonRequestParseFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonRequestParseFailedException(Throwable cause) {
        super(cause);
    }

}
