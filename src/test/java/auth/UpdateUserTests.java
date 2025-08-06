package auth;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import base.LoginUserTestBase;
import constants.ErrorMessages;
import models.auth.UserInfoResponse;
import models.auth.UserInfoUpdateRequest;
import models.errors.ErrorResponse;

import java.io.FileOutputStream;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserTests extends LoginUserTestBase {

    protected UserInfoUpdateRequest updatedUserInfo;

    private PrintStream logStream;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.updatedUserInfo = this.dataHelper.createUpdatedUserData();

        // Создаем поток для логирования в файл
        try {
            logStream = new PrintStream(new FileOutputStream("test_logs.log", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Не забудьте закрывать поток после всех тестов
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        if (logStream != null) {
            logStream.close();
        }
    }

    @Test
    @DisplayName("Проверка обновления информации пользователя")
    @Description("Позитивная проверка возможности обновить данные пользователя")
    public void userInfoCanBeUpdatedTest() {
        Response updatedUserInfoResponse = this.authClient.updateUser(this.registeredUser.getAccessToken(), this.updatedUserInfo)
                .then()
                .log().all() // Логируем запрос и ответ
                .extract()
                .response();

        // Также можно явно вывести в лог
        logStream.println("Response status: " + updatedUserInfoResponse.statusCode());

        assertEquals(HttpStatus.SC_OK, updatedUserInfoResponse.statusCode(), "Неверный статус-код");

        UserInfoResponse userInfoResponse = updatedUserInfoResponse.as(UserInfoResponse.class);
        logStream.println("Parsed UserInfoResponse: " + userInfoResponse);

        assertTrue(userInfoResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.updatedUserInfo.getEmail(), userInfoResponse.getUser().getEmail(), "Email не совпадает");
        assertEquals(this.updatedUserInfo.getName(), userInfoResponse.getUser().getName(), "Имя не совпадает");
    }

    @Test
    @DisplayName("Проверка невозможности обновления информации пользователя")
    @Description("Проверка невозможности обновить данные пользователя без авторизации")
    public void unableToUpdateUserInfoWithoutAuthorizationTest() {
        Response updatedUserInfoResponse = this.authClient.updateUser("", this.updatedUserInfo)
                .then()
                .log().all()
                .extract()
                .response();

        logStream.println("Response status: " + updatedUserInfoResponse.statusCode());

        assertEquals(HttpStatus.SC_UNAUTHORIZED, updatedUserInfoResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = updatedUserInfoResponse.as(ErrorResponse.class);
        logStream.println("Error response: " + errorResponse);

        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("You should be authorised", ErrorMessages.USER_NOT_AUTHORIZED, errorResponse.getMessage());
    }
}
/* package auth;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import base.LoginUserTestBase;
import constants.ErrorMessages;
import models.auth.UserInfoResponse;
import models.auth.UserInfoUpdateRequest;
import models.errors.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserTests extends LoginUserTestBase {

    protected UserInfoUpdateRequest updatedUserInfo;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.updatedUserInfo = this.dataHelper.createUpdatedUserData();
    }

    @Test
    @DisplayName("Проверка обновления информации пользователя")
    @Description("Позитивная проверка возможности обновить данные пользователя")
    public void userInfoCanBeUpdatedTest() {
        Response updatedUserInfoResponse = this.authClient.updateUser(this.registeredUser.getAccessToken(), this.updatedUserInfo);
        assertEquals(HttpStatus.SC_OK, updatedUserInfoResponse.statusCode(), "Неверный статус-код");

        UserInfoResponse userInfoResponse = updatedUserInfoResponse.as(UserInfoResponse.class);
        assertTrue(userInfoResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Неверное значение поля 'email'", this.updatedUserInfo.getEmail(), userInfoResponse.getUser().getEmail());
        assertEquals("Неверное значение поля 'name'", this.updatedUserInfo.getName(), userInfoResponse.getUser().getName());
    }

    @Test
    @DisplayName("Проверка невозможности обновления информации пользователя")
    @Description("Проверка невозможности обновить данные пользователя без авторизации")
    public void unableToUpdateUserInfoWithoutAuthorizationTest() {
        Response updatedUserInfoResponse = this.authClient.updateUser("", this.updatedUserInfo);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, updatedUserInfoResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = updatedUserInfoResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("You should be authorised", ErrorMessages.USER_NOT_AUTHORIZED, errorResponse.getMessage());

    }
} */
