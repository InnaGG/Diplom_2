package ru.yandex.praktikum.user;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.praktikum.TestSteps;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.pojo.CreateUserRequest;
import ru.yandex.praktikum.pojo.LoginUserRequest;

import static ru.yandex.praktikum.generator.CreateUserGenerator.getRandomCreateUserRequest;
import static ru.yandex.praktikum.generator.LoginUserGenerator.getLoginUserRequest;

public class LoginUserTest {
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
    @DisplayName("Login User")
    public void loginUserPositive() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = testSteps.createUserAndReturnResponse(createUserRequest);
        LoginUserRequest loginUserRequest = getLoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
        testSteps.loginAndReturnResponse(loginUserRequest);
        accessToken = testSteps.getAnAccessTokenFromResponse(createUserResponse);
    }

    @Test
    @DisplayName("Login User with incorrect email. Negative case")
    public void loginUserWithIncorrectEmailNegative() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        String randomIncorrectEmail = RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + "." + RandomStringUtils.randomAlphabetic(3);
        String password = createUserRequest.getPassword();
        LoginUserRequest loginUserRequest = getLoginUserRequest(randomIncorrectEmail, password);
        userClient.loginUser(loginUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login User with incorrect password. Negative case.")
    public void loginUserWithIncorrectPasswordNegative() {
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        String randomIncorrectEmail = createUserRequest.getEmail();
        String password = RandomStringUtils.randomAlphabetic(10);
        LoginUserRequest loginUserRequest = getLoginUserRequest(randomIncorrectEmail, password);
        userClient.loginUser(loginUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }
}
