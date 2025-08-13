package models.orders;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String _id;
    private String createdAt;
    private List<Ingredient> ingredients;
    private String name;
    private int number;
    private Owner owner;
    private int price;
    private String status;
    private String updatedAt;
}

/* import java.util.List;

public class Order {
    private String _id;
    private String createdAt;
    private List<Ingredient> ingredients;
    private String name;
    private int number;
    private Owner owner;
    private int price;
    private String status;
    private String updatedAt;

    public Order(String _id, String createdAt, List<Ingredient> ingredients, String name, int number, Owner owner, int price, String status, String updatedAt) {
        this._id = _id;
        this.createdAt = createdAt;
        this.ingredients = ingredients;
        this.name = name;
        this.number = number;
        this.owner = owner;
        this.price = price;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Order() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
} */
