package models.auth;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutResponse {
    private boolean success;
    private String message;
}

/* public class UserLogoutResponse {
    private boolean success;
    private String message;

    public UserLogoutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserLogoutResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} */
