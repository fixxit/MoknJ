package nl.it.fixx.moknj.response;

public class ErrorResponse extends Response {

    @Override
    public String toString() {
        return "ErrorResponse{" + "success=" + success
                + ", action='" + action + '\''
                + ", method='" + method + '\''
                + ", message='" + message + '}';
    }
}
