package ru.yandex.praktikum.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.praktikum.pojo.CreateUserRequest;

public class CreateUserGenerator {

    public static CreateUserRequest getRandomCreateUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + "." + RandomStringUtils.randomAlphabetic(3));
        createUserRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        createUserRequest.setName(RandomStringUtils.randomAlphabetic(10));
        return createUserRequest;
    }

    public static CreateUserRequest getCreateUserRequest(String email, String password, String name) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setPassword(password);
        createUserRequest.setName(name);
        return createUserRequest;
    }

    public static CreateUserRequest getCreateUserRequestWithMissingEmailNegative(String password, String name) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setPassword(password);
        createUserRequest.setName(name);
        return createUserRequest;
    }

    public static CreateUserRequest getCreateUserRequestWithMissingPasswordNegative(String email, String name) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setName(name);
        return createUserRequest;
    }
}
