package orders;

import dto.DtoOrder;
import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserSteps.createUser;
import static utils.Utils.BASE_URL;

public class CreateOrderTest {

    public ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    String email = "user10@ya.ru";
    String password = "pass123";
    String name = "Naruto";

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами авторизованного пользователя")
    public void createOrderWithIngredients() {
        DtoUser user = new DtoUser(email, password, name);
        String token = createUser(email, password, name);
        DtoOrder request = new DtoOrder(ingredients);

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/orders");

        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("createOrderResponseJsonScheme.json"))
                .body("success", equalTo(true))
                .body("name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.number", notNullValue());
    }
}
