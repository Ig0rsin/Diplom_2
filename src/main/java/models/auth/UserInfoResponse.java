package models.auth;

public class UserInfoResponse {
    private boolean success;
    private User user;

    public UserInfoResponse(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public UserInfoResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
