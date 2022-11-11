package ru.yandex.praktikum.client;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.pojo.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestClient {
    private static final String CREATE_AN_ORDER = "/api/orders";
    private static final String GET_ORDERS = "/api/orders";

    public ValidatableResponse createOrderWithoutAuthorization(CreateOrderRequest createOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(createOrderRequest)
                .post(CREATE_AN_ORDER)
                .then();
    }

    public ValidatableResponse createOrderWithAuthorization(CreateOrderRequest createOrderRequest, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .body(createOrderRequest)
                .post(CREATE_AN_ORDER)
                .then();
    }

    public ValidatableResponse getOrdersListWithAuthorization(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .get(GET_ORDERS)
                .then();
    }

    public ValidatableResponse getOrdersListWithoutAuthorizationNEGATIVE() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(GET_ORDERS)
                .then();
    }
}
