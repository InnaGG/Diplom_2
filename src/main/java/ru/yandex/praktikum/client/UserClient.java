package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.pojo.CreateUserRequest;
import ru.yandex.praktikum.pojo.LoginUserRequest;
import ru.yandex.praktikum.pojo.UpdateUserRequest;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String CHANGE_USER = "/api/auth/user";
    private static final String DELETE_USER = "/api/auth/user";

    @Step("Create User Request")
    public ValidatableResponse createUser(CreateUserRequest createUserRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(createUserRequest)
                .post(CREATE_USER)
                .then();
    }

    @Step("Login User Request")
    public ValidatableResponse loginUser(LoginUserRequest loginUserRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginUserRequest)
                .post(LOGIN_USER)
                .then();
    }

    @Step("Update User Request")
    public ValidatableResponse updateUser(UpdateUserRequest updateUserRequest, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .body(updateUserRequest)
                .patch(CHANGE_USER)
                .then();
    }

    @Step("Delete User Request")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .delete(DELETE_USER)
                .then();
    }

    @Step("Update User Without Authorization Negative Request")
    public ValidatableResponse updateUserWithoutAuthorizationNEGATIVE(UpdateUserRequest updateUserRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(updateUserRequest)
                .patch(CHANGE_USER)
                .then();
    }
}
