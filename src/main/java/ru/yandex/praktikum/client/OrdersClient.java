package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.pojo.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestClient {
    private static final String CREATE_AN_ORDER = "/api/orders";
    private static final String GET_ORDERS = "/api/orders";

    @Step("Create Order Without Authorization Request")
    public ValidatableResponse createOrderWithoutAuthorization(CreateOrderRequest createOrderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(createOrderRequest)
                .post(CREATE_AN_ORDER)
                .then();
    }

    @Step("Create Order With Authorization Request")
    public ValidatableResponse createOrderWithAuthorization(CreateOrderRequest createOrderRequest, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .body(createOrderRequest)
                .post(CREATE_AN_ORDER)
                .then();
    }

    @Step("Get Orders List With Authorization Request")
    public ValidatableResponse getOrdersListWithAuthorization(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .get(GET_ORDERS)
                .then();
    }

    @Step("Get Order's List Without Authorization Request Negative")
    public ValidatableResponse getOrdersListWithoutAuthorizationNEGATIVE() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(GET_ORDERS)
                .then();
    }
}
