package steps;

import dto.DtoOrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class OrderSteps {

    @Step("Получение данных об ингредиентах")
    public static Response getIngredients() {
        return given()
                .when()
                .get(BASE_URL + "/ingredients");
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + "/orders");
    }

    @Step("Создание заказа авторизованного пользовател")
    public static Response createOrderByAuthUser(DtoOrderRequest dtoOrderRequest, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(dtoOrderRequest)
                .when()
                .post(BASE_URL + "/orders");
    }

    @Step("Создание заказа неавторизованного пользовател")
    public static Response createOrderByUnauthUser(DtoOrderRequest dtoOrderRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(dtoOrderRequest)
                .when()
                .post(BASE_URL + "/orders");
    }
}
