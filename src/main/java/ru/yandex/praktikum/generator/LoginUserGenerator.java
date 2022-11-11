package ru.yandex.praktikum.generator;

import ru.yandex.praktikum.pojo.LoginUserRequest;

public class LoginUserGenerator {

    public static LoginUserRequest getLoginUserRequest(String email, String password) {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(email);
        loginUserRequest.setPassword(password);
        return loginUserRequest;
    }
}
