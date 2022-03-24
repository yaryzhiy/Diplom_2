package steps;

import dto.DtoIngredientsResponse;
import dto.DtoOrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class OrderSteps {

    @Step("Получение данных об ингредиентах")
    public static DtoIngredientsResponse getIngredientsData() {
        Response response = given()
                .when()
                .get(BASE_URL + "/ingredients");

        response.then()
                .statusCode(200);

        return response.getBody().as(DtoIngredientsResponse.class);
    }

    @Step("Получение заказов пользователя")
    public static int getUserOrders(String token) {
        Response response = given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + "/orders");

        response.then()
                .statusCode(200);

        return response.then().extract().path("orders[0].number");
    }

    @Step("Создание заказа")
    public static int createOrder(String token, ArrayList<String> ingredients) {
        DtoOrderRequest request = new DtoOrderRequest(ingredients);

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/orders");

        response.then()
                .statusCode(200);

        return response.then().extract().path("order.number");
    }
}
