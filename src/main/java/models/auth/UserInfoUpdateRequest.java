package models.auth;

public class UserInfoUpdateRequest {
    private String email;
    private String name;

    public UserInfoUpdateRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserInfoUpdateRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
