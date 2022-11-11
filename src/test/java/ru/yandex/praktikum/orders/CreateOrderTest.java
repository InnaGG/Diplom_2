package ru.yandex.praktikum.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.TestSteps;
import ru.yandex.praktikum.client.OrdersClient;
import ru.yandex.praktikum.pojo.CreateOrderRequest;
import ru.yandex.praktikum.pojo.CreateUserRequest;

import java.util.ArrayList;

import static ru.yandex.praktikum.generator.CreateOrderGenerator.getCreateOrderRequest;
import static ru.yandex.praktikum.generator.CreateUserGenerator.getRandomCreateUserRequest;

public class CreateOrderTest {
    private OrdersClient ordersClient;
    private TestSteps testSteps;
    private String accessToken;

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        testSteps = new TestSteps();
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            testSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Create order without authorisation")
    public void createAnOrderWithoutAuthorisationPositive() {
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6d");
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6f");
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithoutAuthorization(createOrder)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.equalTo(true))
                .body("name", Matchers.notNullValue())
                .body("order", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Create order with authorisation")
    public void createAnOrderWithAuthorisationPositive() {
        //create a user
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(validatableResponse);
        //create an order
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6d");
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6f");
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithAuthorization(createOrder, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.equalTo(true))
                .body("name", Matchers.notNullValue())
                .body("order", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Create order without ingredients and authorisation. Negative case")
    public void createAnOrderWithoutIngredientsWithoutAuthorizationNegative() {
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithoutAuthorization(createOrder)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order without ingredients and with authorisation. Negative case")
    public void createAnOrderWithoutIngredientsWithAuthorizationNegative() {
        //create a user
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(validatableResponse);
        //create an order
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithAuthorization(createOrder, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with incorrect ingredients hash id")
    public void createAnOrderWithIncorrectIngredientIdsNegative() {
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        listOfIngredientsId.add("61c0c5a71d1faaa6d");
        listOfIngredientsId.add("61a71d1f82001bdaaa6f");
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithoutAuthorization(createOrder)
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Get Order list of User with authorization")
    public void getOrdersOfUserWithAuthorizationPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(validatableResponse);
        //create an order
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6d");
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6f");
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithAuthorization(createOrder, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.equalTo(true))
                .body("name", Matchers.notNullValue())
                .body("order", Matchers.notNullValue());
        //check orders list
        ordersClient.getOrdersListWithAuthorization(accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.equalTo(true))
                .body("orders", Matchers.notNullValue())
                .body("total", Matchers.notNullValue())
                .body("totalToday", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Get Order list of User without authorization. Negative case")
    public void getOrdersOfUserWithoutAuthorizationNegative() {
        //create an order
        ArrayList<String> listOfIngredientsId = new ArrayList<>();
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6d");
        listOfIngredientsId.add("61c0c5a71d1f82001bdaaa6f");
        CreateOrderRequest createOrder = getCreateOrderRequest(listOfIngredientsId);
        ordersClient.createOrderWithoutAuthorization(createOrder)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.equalTo(true))
                .body("name", Matchers.notNullValue())
                .body("order", Matchers.notNullValue());
        //check orders list
        ordersClient.getOrdersListWithoutAuthorizationNEGATIVE()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
