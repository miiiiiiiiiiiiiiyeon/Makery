package makery.makerspace.t.makery.model.request;

/**
 * 큰 그릇 하나
 */

public class RequestModel<T> {
    private int success;
    private String message;
    private T result;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {

        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
