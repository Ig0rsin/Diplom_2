package auth;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import base.LoginUserTestBase;
import constants.ErrorMessages;
import models.auth.UserInfoResponse;
import models.auth.UserInfoUpdateRequest;
import models.errors.ErrorResponse;

public class UpdateUserTest extends LoginUserTestBase {

    protected UserInfoUpdateRequest updatedUserInfo;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.updatedUserInfo = this.dataHelper.createUpdatedUserData();
    }

    @Test
    @DisplayName("Изменение данных пользователя с автоизацией")
    @Description("Позитивная проверка возможности обновить данные пользователя")
    @Step("Проверка возможности обновления информации пользователя")
    public void userInfoCanBeUpdatedTest() {
        Response updatedUserInfoResponse = executeUpdateUser(this.registeredUser.getAccessToken(), this.updatedUserInfo);
        assertEquals(HttpStatus.SC_OK, updatedUserInfoResponse.statusCode(), "Неверный статус-код");

        UserInfoResponse userInfoResponse = updatedUserInfoResponse.as(UserInfoResponse.class);
        assertTrue(userInfoResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.updatedUserInfo.getEmail(), userInfoResponse.getUser().getEmail(), "Некорректный email");
        assertEquals(this.updatedUserInfo.getName(), userInfoResponse.getUser().getName(), "Некорректное имя");
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка невозможности обновить данные пользователя без авторизации")
    @Step("Попытка обновления информации без авторизации")
    public void unableToUpdateUserInfoWithoutAuthorizationTest() {
        Response updatedUserInfoResponse = executeUpdateUser("", this.updatedUserInfo);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, updatedUserInfoResponse.statusCode(), "Неверный статус-код");
        ErrorResponse errorResponse = updatedUserInfoResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("You should be authorised", ErrorMessages.USER_NOT_AUTHORIZED, errorResponse.getMessage());
    }

    @Step("Обновление информации пользователя с токеном: {token}")
    private Response executeUpdateUser(String token, UserInfoUpdateRequest userData) {
        return this.authClient.updateUser(token, userData);
    }
// коммент для гитхаба
}