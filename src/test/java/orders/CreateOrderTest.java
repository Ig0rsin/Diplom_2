package orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat; // мэтчеры
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

import base.LoginUserTestBase;
import clients.OrderClient;
import constants.ErrorMessages;
import models.errors.ErrorResponse;
import models.orders.OrderCreateRequest;
import models.orders.IngredientsResponse;
import models.orders.OrderCreateResponse;

public class CreateOrderTest extends LoginUserTestBase {

    private OrderClient orderClient;
    private IngredientsResponse ingredients;
    private OrderCreateRequest order;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.orderClient = new OrderClient();
        Response ingredientsResponse = this.orderClient.getIngredients();
        ingredientsResponse.then().statusCode(HttpStatus.SC_OK);
        this.ingredients = ingredientsResponse.as(IngredientsResponse.class);
        this.order = this.dataHelper.createOrder(this.ingredients.getData());
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов с авторизацией")
    @Description("Проверка невозможности создать пустой заказ авторизованным пользователем")
    @Step("Проверка невозможности создать пустой заказ с авторизацией")
    public void unableToCreateEmptyOrderWithAuthorizationTest() {
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = executeCreateOrder(this.registeredUser.getAccessToken(), emptyOrder);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Ingredient ids must be provided", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов без авторизации")
    @Description("Проверка невозможности создать пустой заказ неавторизованным пользователем")
    @Step("Проверка невозможности создать пустой заказ без авторизации")
    public void unableToCreateEmptyOrderWithoutAuthorizationTest() {
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = executeCreateOrder("", emptyOrder);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Ingredient ids must be provided", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка возможности создать заказ авторизованным пользователем")
    @Step("Создание заказа с ингредиентами и авторизацией")
    public void createOrderWithIngredientsWithAuthorizationTest() {
        Response createOrderResponse = executeCreateOrder(this.registeredUser.getAccessToken(), this.order);
        assertEquals(HttpStatus.SC_OK, createOrderResponse.statusCode(), "Неверный статус-код");

        OrderCreateResponse orderResponse = createOrderResponse.as(OrderCreateResponse.class);
        assertTrue(orderResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.ingredients.getData().size(), orderResponse.getOrder().getIngredients().size(), "Неверное количество ингредиентов");
    }

    @Test
    @DisplayName("Создание заказа с невалидными хешами")
    @Description("Проверка невозможности создания заказа с невалидными хешами ингредиентов")
    @Step("Создание заказа с неправильными хешами ингредиентов")
    public void unableToCreateOrderWithWrongIngredientsHashWithAuthorizationTest() {
        OrderCreateRequest orderWithWrongHash = new OrderCreateRequest(Arrays.asList("111111111111111111111111", "222222222222222222222222"));
        Response createOrderResponse = executeCreateOrder(this.registeredUser.getAccessToken(), orderWithWrongHash);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("One or more ids provided are incorrect", ErrorMessages.CREATE_ORDER_WRONG_HASH, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя с авторизацией")
    @Description("Проверка возможности получить заказ авторизованным пользователем")
    @Step("Создание заказа и получение списка заказов для авторизованного пользователя")
    public void getOrderWithIngredientsWithAuthorizationTest() {
        Response createOrderResponse = executeCreateOrder(this.registeredUser.getAccessToken(), this.order);
        createOrderResponse.then().statusCode(HttpStatus.SC_OK);

        Response getOrdersresponse = this.orderClient.getOrders(this.registeredUser.getAccessToken());
        assertEquals(HttpStatus.SC_OK, getOrdersresponse.statusCode(), "Неверный статус-код");

        // Используем Hamcrest assertion для body
        getOrdersresponse.then()
                .assertThat()
                .body("orders._id", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    @Description("Проверка невозможности получить заказ неавторизованным пользователем")
    @Step("Попытка получить заказы без авторизации")
    public void unableToGetOrderWithIngredientsWithoutAuthorizationTest() {
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), this.order);
        createOrderResponse.then().statusCode(HttpStatus.SC_OK);
        Response getOrdersresponse = this.orderClient.getOrders("");
        assertEquals(HttpStatus.SC_UNAUTHORIZED, getOrdersresponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = getOrdersresponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("You should be authorised", ErrorMessages.USER_NOT_AUTHORIZED, errorResponse.getMessage());
    }

    @Step("Создать заказ с токеном: {token}")
    private Response executeCreateOrder(String token, OrderCreateRequest order) {
        return this.orderClient.createOrder(token, order);
    }
// коммент для гитхаба
}