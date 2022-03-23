package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class OrderSteps {

    @Step("Получение данных об ингредиентах")
    public Object[] getIngredientsData() {
        Response response = given()
                .when()
                .get(BASE_URL + "/ingredients");

        response.then()
                .statusCode(200);

        Object[] data = response.then()
                .extract()
                .path("data");

        return new Object[] {data[0], data[1]};
    }
}
