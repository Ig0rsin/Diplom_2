package orders;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat; // мэтчеры
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import base.LoginUserTestBase;
import clients.OrderClient;
import constants.ErrorMessages;
import models.errors.ErrorResponse;
import models.orders.OrderCreateRequest;
import models.orders.IngredientsResponse;
import models.orders.OrderCreateResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateOrderTests extends LoginUserTestBase {

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
    @DisplayName("Проверка невозможности создания пустого заказа с авторизацией")
    @Description("Проверка невозможности создать пустой заказ авторизованным пользователем")
    public void unableToCreateEmptyOrderWithAuthorizationTest() {
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), emptyOrder);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка невозможности создания пустого заказа без авторизацией")
    @Description("Проверка невозможности создать пустой заказ неавторизованным пользователем")
    public void unableToCreateEmptyOrderWithoutAuthorizationTest() {
        OrderCreateRequest emptyOrder = new OrderCreateRequest(new ArrayList<>());
        Response createOrderResponse = this.orderClient.createOrder("", emptyOrder);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WITHOUT_ID, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка возможности создания заказа")
    @Description("Проверка возможности создать заказ авторизованным пользователем")
    public void createOrderWithIngredientsWithAuthorizationTest() {
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), this.order);
        assertEquals(HttpStatus.SC_OK, createOrderResponse.statusCode(), "Неверный статус-код");

        OrderCreateResponse orderResponse = createOrderResponse.as(OrderCreateResponse.class);
        assertTrue(orderResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals(this.ingredients.getData().size(), orderResponse.getOrder().getIngredients().size(), "Неверное количество ингредиентов");
    }

    @Test
    @DisplayName("Проверка невозможности создания заказа с невалидными хешами")
    @Description("Проверка невозможности создания заказа с невалидными хешами ингредиентов")
    public void unableToCreateOrderWithWrongIngredientsHashWithAuthorizationTest() {
        OrderCreateRequest orderWithWrongHash = new OrderCreateRequest(Arrays.asList("1111111111111111111111", "22222222222222222222"));
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), orderWithWrongHash);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderResponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = createOrderResponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Неверное значение поля 'message'", ErrorMessages.CREATE_ORDER_WRONG_HASH, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка возможности получения заказа")
    @Description("Проверка возможности получить заказ авторизованным пользователем")
    public void getOrderWithIngredientsWithAuthorizationTest() {
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), this.order);
        createOrderResponse.then().statusCode(HttpStatus.SC_OK);

        Response getOrdersresponse = this.orderClient.getOrders(this.registeredUser.getAccessToken());
        assertEquals(HttpStatus.SC_OK, getOrdersresponse.statusCode(), "Неверный статус-код");

        // Используем Hamcrest assertion для body
        getOrdersresponse.then()
                .assertThat()
                .body("orders._id", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Проверка невозможности получения заказа")
    @Description("Проверка невозможности получить заказ неавторизованным пользователем")
    public void unableToGetOrderWithIngredientsWithoutAuthorizationTest() {
        Response createOrderResponse = this.orderClient.createOrder(this.registeredUser.getAccessToken(), this.order);
        createOrderResponse.then().statusCode(HttpStatus.SC_OK);

        Response getOrdersresponse = this.orderClient.getOrders("");
        assertEquals(HttpStatus.SC_UNAUTHORIZED, getOrdersresponse.statusCode(), "Неверный статус-код");

        ErrorResponse errorResponse = getOrdersresponse.as(ErrorResponse.class);
        assertFalse(errorResponse.isSuccess(), "Неверное значение поля 'success'");
        assertEquals("Неверное значение поля 'message'", ErrorMessages.USER_NOT_AUTHORIZED, errorResponse.getMessage());
    }
}