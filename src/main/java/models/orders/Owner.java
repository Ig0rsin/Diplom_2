package models.orders;

public class Owner {
    private String createdAt;
    private String email;
    private String name;
    private String updatedAt;

    public Owner(String createdAt, String email, String name, String updatedAt) {
        this.createdAt = createdAt;
        this.email = email;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public Owner() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
