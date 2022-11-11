package ru.yandex.praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.TestSteps;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.pojo.CreateUserRequest;
import ru.yandex.praktikum.pojo.UpdateUserRequest;

import static ru.yandex.praktikum.generator.CreateUserGenerator.getRandomCreateUserRequest;
import static ru.yandex.praktikum.generator.UpdateUserGenerator.getUpdateUserRequestAllParameters;

public class UpdateUserTest {

    private UserClient userClient;
    private TestSteps testSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        testSteps = new TestSteps();
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            testSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Update User - all fields")
    public void updateUserAllFieldsPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
        CreateUserRequest createNonExistentUserRequest = getRandomCreateUserRequest();
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createNonExistentUserRequest.getEmail(), createNonExistentUserRequest.getPassword(), createNonExistentUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Update User - email field")
    public void updateUserEmailFieldPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(getRandomCreateUserRequest().getEmail(), createUserRequest.getPassword(), createUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Update User - password field")
    public void updateUserPasswordFieldPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createUserRequest.getEmail(), getRandomCreateUserRequest().getPassword(), createUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Update User - name field")
    public void updateUserNameFieldsPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createUserRequest.getEmail(), createUserRequest.getPassword(), getRandomCreateUserRequest().getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Update User - name field. Without Authorization, negative case")
    public void updateUserNameWithoutAuthorizationNegative() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
        CreateUserRequest createNonExistentUserRequest = getRandomCreateUserRequest();
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createNonExistentUserRequest.getEmail(), createNonExistentUserRequest.getPassword(), createNonExistentUserRequest.getName());
        userClient.updateUserWithoutAuthorizationNEGATIVE(updateUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
