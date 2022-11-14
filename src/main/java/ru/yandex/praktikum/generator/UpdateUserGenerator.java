package ru.yandex.praktikum.generator;

import ru.yandex.praktikum.pojo.UpdateUserRequest;

public class UpdateUserGenerator {

    public static UpdateUserRequest getUpdateUserRequestEmail(String email) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail(email);
        return updateUserRequest;
    }

    public static UpdateUserRequest getUpdateUserRequestPassword(String password) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPassword(password);
        return updateUserRequest;
    }

    public static UpdateUserRequest getUpdateUserRequestName(String name) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName(name);
        return updateUserRequest;
    }

    public static UpdateUserRequest getUpdateUserRequestAllParameters(String email, String password, String name) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail(email);
        updateUserRequest.setPassword(password);
        updateUserRequest.setName(name);
        return updateUserRequest;
    }
}
