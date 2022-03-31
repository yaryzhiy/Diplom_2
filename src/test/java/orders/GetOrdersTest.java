package orders;

import dto.DtoOrderRequest;
import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.OrderSteps.createOrderByAuthUser;
import static steps.OrderSteps.getUserOrders;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;
import static utils.Utils.BASE_URL;

public class GetOrdersTest {

    String email = "user40@ya.ru";
    String password = "pass123";
    String name = "Naruto";
    public ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    String token;

    @Test
    @DisplayName("Успешное получение заказов авторизованного пользователя")
    public void getOrdersSuccessTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");
        int orderNumber = createOrderByAuthUser(new DtoOrderRequest(ingredients), token).then().extract().path("order.number");

        Response response = getUserOrders(token);
        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("getOrdersJsonScheme.json"))
                .body("success", equalTo(true))
                .body("orders[0]._id", notNullValue())
                .body("orders[0].ingredients", equalTo(ingredients))
                .body("orders[0].status", equalTo("done"))
                .body("orders[0].name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .body("orders[0].number", equalTo(orderNumber))
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Ошибка получения заказов без авторизации")
    public void getOrdersWithoutAuthorizationErrorTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URL + "/orders");

        response.then()
                .assertThat()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser(token);
            token = null;
        }
    }
}
