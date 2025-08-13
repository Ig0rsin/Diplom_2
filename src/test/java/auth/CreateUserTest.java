package auth;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

import base.TestBase;
import constants.ErrorMessages;
import models.auth.UserRegisterRequest; // отсюда запрос реги
import models.auth.UserRegisterResponse; // отсюда запрос ответа реги
import models.errors.ErrorResponse;

public class CreateUserTest extends TestBase {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Позитивная проверка возможности создания уникального пользователя")
    public void userCanBeCreatedSuccessfullyTest() {
        UserRegisterRequest userRequest = this.dataHelper.createRandomUser(); // данные юзера
        Response registerUserResponse = registerUser(userRequest); // рега нового юзера
        checkStatusCode(registerUserResponse, HttpStatus.SC_OK); // статус - ок
        UserRegisterResponse registeredUser = parseRegisterResponse(registerUserResponse); // десериализация ответа

        verifyUserCreationSuccess(registeredUser, userRequest); // проверка полей
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка невозможности создания двух пользователей с одинаковыми данными")
    public void unableToCreateTwoSameUsersTest() {
        UserRegisterRequest userRequest = this.dataHelper.createRandomUser();

        Response firstResponse = registerUser(userRequest); // рега первого юзера
        checkStatusCode(firstResponse, HttpStatus.SC_OK);
        UserRegisterResponse registeredUser = parseRegisterResponse(firstResponse);

        Response secondResponse = registerUser(userRequest); // рега того же юзера повторно
        checkStatusCode(secondResponse, HttpStatus.SC_FORBIDDEN);
        ErrorResponse errorResponse = parseErrorResponse(secondResponse);
        verifyErrorResponse(errorResponse, false, ErrorMessages.CREATE_USER_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным одним из обязательных полей")
    @Description("Проверка невозможности создания пользователя без обязательного поля пароль")
    public void unableToCreateUserWithoutPasswordTest() {
        UserRegisterRequest userWithoutPassword = this.dataHelper.createUserWithoutPassword();

        Response response = registerUser(userWithoutPassword);
        checkStatusCode(response, HttpStatus.SC_FORBIDDEN);
        ErrorResponse errorResponse = parseErrorResponse(response);
        verifyErrorResponse(errorResponse, false, ErrorMessages.CREATE_USER_NOT_ENOUGH_DATA);
    }

    @Step("Регистрация пользователя с данными: {userRequest}")
    private Response registerUser(UserRegisterRequest userRequest) {
        return authClient.registerUser(userRequest)
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

    @Step("Десериализация ответа в объект UserRegisterResponse")
    private UserRegisterResponse parseRegisterResponse(Response response) {
        return response.as(UserRegisterResponse.class);
    }

    @Step("Десериализация ошибки из ответа")
    private ErrorResponse parseErrorResponse(Response response) {
        return response.as(ErrorResponse.class);
    }

    @Step("Проверка успешного создания пользователя. Имя: {expectedName}")
    private void verifyUserCreationSuccess(UserRegisterResponse registeredUser, UserRegisterRequest expectedRequest) {
        String expectedName = expectedRequest.getName();
        assertTrue(registeredUser.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(expectedRequest.getName(), registeredUser.getUser().getName(), "Неверное значение поля 'name'");
    }

    @Step("Проверка ответа об ошибке. success: {expectedSuccess}, message: {expectedMessage}")
    private void verifyErrorResponse(ErrorResponse errorResponse, boolean expectedSuccess, String expectedMessage) {
        assertEquals(expectedSuccess, errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(expectedMessage, errorResponse.getMessage(), "Неверное сообщение об ошибке");
    }
}