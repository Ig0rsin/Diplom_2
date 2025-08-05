package utils;

import com.github.javafaker.Faker;
import models.auth.UserInfoUpdateRequest;
import models.auth.UserRegisterRequest;
import models.orders.OrderCreateRequest;
import models.orders.Ingredient;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DataHelper {
    private final Faker faker;

    public DataHelper() {
        this.faker = new Faker(new Locale("en"));
    }

    public UserRegisterRequest createRandomUser() {
        String email = generateEmail();
        String password = generatePassword();
        String name = email;
        return new UserRegisterRequest(email, password, name);
    }

    public UserRegisterRequest createUserWithoutPassword() {
        String email = generateEmail();
        String password = null;
        String name = email;
        return new UserRegisterRequest(email, password, name);
    }

    public UserInfoUpdateRequest createUpdatedUserData() {
        String email = generateEmail();
        String name = email;
        return new UserInfoUpdateRequest(email, name);
    }

    public OrderCreateRequest createOrder(List<Ingredient> ingredients){
        List<String> ingredientIds = ingredients.stream()
                .map(Ingredient::get_id)
                .collect(Collectors.toList());
        return new OrderCreateRequest(ingredientIds);
    }

    private String generateEmail() {
        return faker.internet().emailAddress();
    }

    private String generatePassword() {
        return faker.internet().password(8, 12, true, true, true);
    }
}
