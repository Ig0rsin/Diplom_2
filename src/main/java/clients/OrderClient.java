package clients;

import io.restassured.response.Response;
import models.orders.OrderCreateRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends ClientBase{

    private static final String API_INGREDIENTS = "/api/ingredients";
    private static final String API_ORDERS = "/api/orders";
    private static final String API_ORDERS_ALL = "/api/orders/all";

    public Response createOrder(String accessToken, OrderCreateRequest orderCreateRequest) {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .when()
                .post(API_ORDERS);
    }

    public Response getOrders(String accessToken){
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(API_ORDERS);
    }

    public Response getIngredients() {
        return given()
                .baseUri(QA_STELLARBURGERS_SERVICE)
                .header("Content-type", "application/json")
                .when()
                .get(API_INGREDIENTS);
    }
}
