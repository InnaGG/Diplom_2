package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.pojo.CreateUserRequest;
import ru.yandex.praktikum.pojo.LoginUserRequest;

public class TestSteps {
    private final UserClient userClient = new UserClient();

    @Step("Create and order and return Validatable Response")
    public ValidatableResponse createUserAndReturnResponse(CreateUserRequest createUserRequest) {
        return userClient.createUser(createUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .body("accessToken", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue());
    }

    @Step("Delete User")
    public void deleteUser(String accessToken) {
        userClient.deleteUser(accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Step("Return an access token from Response")
    public String getAnAccessTokenFromResponse(ValidatableResponse validatableResponse) {
        return validatableResponse.extract()
                .path("accessToken");
    }

    @Step("Login and return Validatable response")
    public ValidatableResponse loginAndReturnResponse(LoginUserRequest loginUserRequest) {
        return userClient.loginUser(loginUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .body("accessToken", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue());
    }
}
