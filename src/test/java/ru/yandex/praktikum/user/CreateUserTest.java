package ru.yandex.praktikum.user;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.praktikum.TestSteps;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.pojo.CreateUserRequest;

import static ru.yandex.praktikum.generator.CreateUserGenerator.getCreateUserRequestWithMissingEmailNegative;
import static ru.yandex.praktikum.generator.CreateUserGenerator.getRandomCreateUserRequest;

public class CreateUserTest {
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
    @DisplayName("Create User")
    public void createUserPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(validatableResponse);
    }

    @Test
    @DisplayName("Create User that was already created")
    public void createUserThatWasAlreadyCreatedNegative() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        userClient.createUser(createUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("User already exists"));
        accessToken = testSteps.getAnAccessTokenFromResponse(validatableResponse);
    }

    @Test
    @DisplayName("Create User with missing mandatory email. Negative case")
    public void createUserWithMissingMandatoryEmailNegative() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        CreateUserRequest userRequestWithMissingEmail = getCreateUserRequestWithMissingEmailNegative(createUserRequest.getPassword(), createUserRequest.getName());
        userClient.createUser(userRequestWithMissingEmail)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }
}
