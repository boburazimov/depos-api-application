package uz.depos.app.service.dto;

/**
 * A DTO representing a response table in rest.
 */
public class ApiResponse {

    private String message;
    private boolean success;
    private Object object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ApiResponse() {}

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(String message, boolean success, Object object) {
        this.message = message;
        this.success = success;
        this.object = object;
    }

    @Override
    public String toString() {
        return "ApiResponse{" + "message='" + message + '\'' + ", success=" + success + ", object=" + object + '}';
    }
}
