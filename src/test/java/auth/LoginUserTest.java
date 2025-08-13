package auth;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import models.auth.UserRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

import base.LoginUserTestBase;
import constants.ErrorMessages;
import models.auth.UserLoginRequest;
import models.auth.UserLoginResponse;
import models.errors.ErrorResponse;

public class LoginUserTest extends LoginUserTestBase {

    @Test
    @DisplayName("Логин существующего пользователя")
    @Description("Позитивная проверка возможности пользователя залогиниться")
    public void userCanLoginSuccessfullyTest() {
        // Создаем объект UserLoginRequest из данных this.user
        UserLoginRequest loginRequest = new UserLoginRequest(this.user.getEmail(), this.user.getPassword());

        Response userLoginResponse = loginUser(loginRequest); // вход с существующими данными
        checkStatusCode(userLoginResponse, HttpStatus.SC_OK);

        UserLoginResponse loggedInUser = parseLoginResponse(userLoginResponse); // десериализация ответа

        verifySuccessfulLogin(loggedInUser, this.user); // проверки
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Проверка невозможности залогиниться с неверным логином и паролем")
    public void unableToLoginWithWrongEmailAndPasswordTest() {

        UserLoginRequest userWithWrongCredentials = new UserLoginRequest( // объект с кривыми данными
                this.user.getEmail() + "asd",
                this.user.getPassword() + "asd"
        );

        Response response = loginUser(userWithWrongCredentials); // вход с кривыми данными
        checkStatusCode(response, HttpStatus.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = parseErrorResponse(response); // десериализация ошибки

        verifyErrorResponse(errorResponse, false, ErrorMessages.LOGIN_USER_INCORRECT_CREDENTIALS); // проверки
    }

    @Step("Вход пользователя с данными: email={loginRequest.email}, password={loginRequest.password}")
    private Response loginUser(UserLoginRequest loginRequest) {
        return authClient.loginUser(loginRequest)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Проверка статуса ответа. Ожидалось: {expectedStatus}")
    private void checkStatusCode(Response response, int expectedStatus) {
        int actualStatus = response.statusCode();
        assertEquals(expectedStatus, actualStatus, "Статус-код не совпадает");
    }

    @Step("Десериализация ответа в объект UserLoginResponse")
    private UserLoginResponse parseLoginResponse(Response response) {
        return response.as(UserLoginResponse.class);
    }

    @Step("Десериализация ошибки из ответа")
    private ErrorResponse parseErrorResponse(Response response) {
        return response.as(ErrorResponse.class);
    }

    @Step("Проверка успешного входа. Имя пользователя: {expectedName}")
    private void verifySuccessfulLogin(UserLoginResponse loggedInUser, UserRegisterRequest expectedUser) {
        assertTrue(loggedInUser.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(expectedUser.getName(), loggedInUser.getUser().getName(), "Имя пользователя не совпадает");
    }

    @Step("Проверка ответа об ошибке. success: {expectedSuccess}, message: {expectedMessage}")
    private void verifyErrorResponse(ErrorResponse errorResponse, boolean expectedSuccess, String expectedMessage) {
        assertEquals(expectedSuccess, errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(expectedMessage, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }
}
