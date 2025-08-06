package models.orders;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsResponse {
    private boolean success;
    private List<Ingredient> data;
}

/* import java.util.List;

public class IngredientsResponse {
    private boolean success;
    private List<Ingredient> data;

    public IngredientsResponse(boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public IngredientsResponse(){
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }
} */
