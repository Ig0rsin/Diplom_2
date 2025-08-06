package auth;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Статик-импорты для assertEquals, assertTrue, assertFalse

import base.TestBase;
import constants.ErrorMessages;
import models.auth.UserRegisterResponse;
import models.errors.ErrorResponse;

public class CreateUserTests extends TestBase {

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Позитивная проверка возможности создания уникального пользователя")
    public void userCanBeCreatedSuccessfullyTest() {
        // Логируем весь запрос и ответ
        Response registerUserResponse = this.authClient.registerUser(this.user)
                .then()
                .log().all() // Логируем запрос и ответ
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, registerUserResponse.statusCode(), "Неверный статус-код");

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);
        assertTrue(this.registeredUser.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.user.getName(), this.registeredUser.getUser().getName(), "Неверное значение поля 'name'");
    }

    @Test
    @DisplayName("Проверка невозможности создания двух одинаковых пользователей")
    @Description("Проверка невозможности создания двух пользователей с одинаковыми данными")
    public void unableToCreateTwoSameUsersTest() {
        // Регистрация первого пользователя с логированием
        Response registerUserResponse = this.authClient.registerUser(this.user)
                .then()
                .log().all()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, registerUserResponse.statusCode(), "Неверный статус-код при регистрации первого пользователя");
        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);

        // Попытка зарегистрировать того же пользователя снова с логированием
        Response sameRegisterUserResponse = this.authClient.registerUser(this.user)
                .then()
                .log().all()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_FORBIDDEN, sameRegisterUserResponse.statusCode(), "Неверный статус-код при попытке создать дублирующего пользователя");

        ErrorResponse errorResponse = sameRegisterUserResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(ErrorMessages.CREATE_USER_ALREADY_EXISTS, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля пароль")
    public void unableToCreateUserWithoutPasswordTest() {
        // Регистрация без пароля с логированием
        Response registerUserResponse = this.authClient.registerUser(this.dataHelper.createUserWithoutPassword())
                .then()
                .log().all()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_FORBIDDEN, registerUserResponse.statusCode(), "Неверный статус-код при создании без пароля");

        ErrorResponse errorResponse = registerUserResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(ErrorMessages.CREATE_USER_NOT_ENOUGH_DATA, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }
}

/* package auth;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Статик-импорты скопом для assertEquals, assertTrue, assertFalse

import base.TestBase;
import constants.ErrorMessages;
import models.auth.UserRegisterResponse;
import models.errors.ErrorResponse;

public class CreateUserTests extends TestBase {

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Позитивная проверка возможности создания уникального пользователя")
    public void userCanBeCreatedSuccessfullyTest() {
        Response registerUserResponse = this.authClient.registerUser(this.user);
        assertEquals(HttpStatus.SC_OK, registerUserResponse.statusCode(), "Неверный статус-код");

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);
        assertTrue(this.registeredUser.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.user.getName(), this.registeredUser.getUser().getName(), "Неверное значение поля 'name'");
    }

    @Test
    @DisplayName("Проверка невозможности создания двух одинаковых пользователей")
    @Description("Проверка невозможности создания двух пользователей с одинаковыми данными")
    public void unableToCreateTwoSameUsersTest() {
        Response registerUserResponse = this.authClient.registerUser(this.user);
        assertEquals(HttpStatus.SC_OK, registerUserResponse.statusCode(), "Неверный статус-код при регистрации первого пользователя");

        this.registeredUser = registerUserResponse.as(UserRegisterResponse.class);

        Response sameRegisterUserResponse = this.authClient.registerUser(this.user);
        assertEquals(HttpStatus.SC_FORBIDDEN, sameRegisterUserResponse.statusCode(), "Неверный статус-код при попытке создать дублирующего пользователя");

        ErrorResponse errorResponse = sameRegisterUserResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(ErrorMessages.CREATE_USER_ALREADY_EXISTS, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя без пароля")
    @Description("Проверка невозможности создания пользователя без обязательного поля пароль")
    public void unableToCreateUserWithoutPasswordTest() {
        Response registerUserResponse = this.authClient.registerUser(this.dataHelper.createUserWithoutPassword());
        assertEquals(HttpStatus.SC_FORBIDDEN, registerUserResponse.statusCode(), "Неверный статус-код при создании без пароля");

        ErrorResponse errorResponse = registerUserResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(ErrorMessages.CREATE_USER_NOT_ENOUGH_DATA, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }
} */
