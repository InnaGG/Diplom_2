package ru.yandex.praktikum.generator;

import ru.yandex.praktikum.pojo.CreateOrderRequest;

import java.util.ArrayList;

public class CreateOrderGenerator {
    public static CreateOrderRequest getCreateOrderRequest(ArrayList<String> ingredients) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setIngredients(ingredients);
        return createOrderRequest;
    }
}
