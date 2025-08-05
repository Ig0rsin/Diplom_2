package auth;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import base.LoginUserTestBase;
import constants.ErrorMessages;
import models.auth.UserLoginRequest;
import models.auth.UserLoginResponse;
import models.errors.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUserTests extends LoginUserTestBase {

    @Test
    @DisplayName("Проверка логина пользователя")
    @Description("Позитивная проверка возможности пользователя залогиниться")
    public void userCanLoginSuccessfullyTest() {
        // здесь новый объект UserLoginRequest из данных this.user (типа UserRegisterRequest, иначе орёт, что метод ждёт другой объект)
        UserLoginRequest loginRequest = new UserLoginRequest(this.user.getEmail(), this.user.getPassword());

        Response userLoginResponse = this.authClient.loginUser(loginRequest);
        assertEquals(HttpStatus.SC_OK, userLoginResponse.statusCode(), "Неверный статус-код");

        UserLoginResponse loggedInUser = userLoginResponse.as(UserLoginResponse.class);
        assertTrue(loggedInUser.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.user.getName(), loggedInUser.getUser().getName(), "Имя пользователя не совпадает");
    }

    @Test
    @DisplayName("Проверка невозможности залогиниться с неверными данными")
    @Description("Проверка невозможности залогиниться с неверным логином и паролем")
    public void unableToLoginWithWrongEmailAndPasswordTest() {
        UserLoginRequest userWithWrongCredentials = new UserLoginRequest(
                this.user.getEmail() + "asd",
                this.user.getPassword() + "asd"
        );
        Response userWithWrongCredentialsResponse = this.authClient.loginUser(userWithWrongCredentials);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, userWithWrongCredentialsResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = userWithWrongCredentialsResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(ErrorMessages.LOGIN_USER_INCORRECT_CREDENTIALS, errorResponse.getMessage());
    }
}
