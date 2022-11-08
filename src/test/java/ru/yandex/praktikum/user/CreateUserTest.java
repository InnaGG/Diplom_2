package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.pojo.CreateOrderRequest;
import ru.yandex.praktikum.pojo.CreateUserRequest;
import ru.yandex.praktikum.pojo.LoginUserRequest;
import ru.yandex.praktikum.pojo.UpdateUserRequest;

import static ru.yandex.praktikum.generator.CreateUserGenerator.*;
import static ru.yandex.praktikum.generator.LoginUserGenerator.*;
import static ru.yandex.praktikum.generator.UpdateUserGenerator.*;

public class CreateUserTest {
    private UserClient userClient;


    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @Step
    public ValidatableResponse createUserAndReturnResponse(CreateUserRequest createUserRequest){
        return  userClient.createUser(createUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .body("accessToken", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue());
    }

    @Step
    public void deleteUser(String accessToken){
        userClient.deleteUser(accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Step
    public String getAnAccessTokenFromResponse(ValidatableResponse validatableResponse){
        return validatableResponse.extract()
                .path("accessToken");
    }

    @Step
    public ValidatableResponse loginAndReturnResponse(LoginUserRequest loginUserRequest){
        return  userClient.loginUser(loginUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .body("accessToken", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue());
    }

    @Test
    public void createUserPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = createUserAndReturnResponse(createUserRequest);
        deleteUser(getAnAccessTokenFromResponse(validatableResponse));
    }

    @Test
    public void createUserThatWasAlreadyCreatedNegative(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse validatableResponse = createUserAndReturnResponse(createUserRequest);
        userClient.createUser(createUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("User already exists"));
        deleteUser(getAnAccessTokenFromResponse(validatableResponse));
    }

    @Test
    public void createUserWithMissingMandatoryEmailNegative(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        CreateUserRequest userRequestWithMissingEmail = getCreateUserRequestWithMissingEmailNegative(createUserRequest.getPassword(), createUserRequest.getName());
        userClient.createUser(userRequestWithMissingEmail)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    public void loginUserPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        LoginUserRequest loginUserRequest = getLoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
        ValidatableResponse loginUserResponse = loginAndReturnResponse(loginUserRequest);
        deleteUser(getAnAccessTokenFromResponse(createUserResponse));
    }

    @Test
    public void loginUserWithIncorrectEmailNegative(){
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
    public void loginUserWithIncorrectPasswordNegative(){
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

    @Test
    public void updateUserAllFieldsPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        String accessToken = getAnAccessTokenFromResponse(createUserResponse);
        CreateUserRequest createNonExistentUserRequest = getRandomCreateUserRequest();
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createNonExistentUserRequest.getEmail(), createNonExistentUserRequest.getPassword(), createNonExistentUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        deleteUser(accessToken);
    }

    @Test
    public void updateUserEmailFieldPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        String accessToken = getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(getRandomCreateUserRequest().getEmail(), createUserRequest.getPassword(), createUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        deleteUser(accessToken);
    }

    @Test
    public void updateUserPasswordFieldPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        String accessToken = getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createUserRequest.getEmail(), getRandomCreateUserRequest().getPassword(), createUserRequest.getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        deleteUser(accessToken);
    }

    @Test
    public void updateUserNameFieldsPositive(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        String accessToken = getAnAccessTokenFromResponse(createUserResponse);
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createUserRequest.getEmail(), createUserRequest.getPassword(), getRandomCreateUserRequest().getName());
        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        deleteUser(accessToken);
    }

    @Test
    public void updateUserNameWithoutAuthorizationNegative(){
        CreateUserRequest createUserRequest = getRandomCreateUserRequest();
        ValidatableResponse createUserResponse = createUserAndReturnResponse(createUserRequest);
        String accessToken = getAnAccessTokenFromResponse(createUserResponse);
        CreateUserRequest createNonExistentUserRequest = getRandomCreateUserRequest();
        UpdateUserRequest updateUserRequest = getUpdateUserRequestAllParameters(createNonExistentUserRequest.getEmail(), createNonExistentUserRequest.getPassword(), createNonExistentUserRequest.getName());
        userClient.updateUserWithoutAuthorizationNEGATIVE(updateUserRequest)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.equalTo(false))
                .body("message", Matchers.equalTo("You should be authorised"));
        deleteUser(accessToken);
    }



}
