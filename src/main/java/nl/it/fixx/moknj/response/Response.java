package nl.it.fixx.moknj.response;

/**
 * Created by colin on 07/10/16 14:42
 */
public abstract class Response {
    protected boolean success;
    protected String action;
    protected String method;
    protected String message;

    public boolean isSuccess() {
	return success;
    }

    public void setSuccess(boolean success) {
	this.success = success;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    public abstract String toString();
}
