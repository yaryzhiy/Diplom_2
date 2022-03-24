package orders;

import dto.DtoIngredientsResponse;
import dto.DtoOrder;
import dto.DtoOrderResponse;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static steps.OrderSteps.getIngredientsData;
import static steps.OrderSteps.getUserOrders;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;
import static utils.Utils.*;

public class CreateOrderTest {

    public ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    String email = "user36@ya.ru";
    String password = "pass123";
    String name = "Naruto";

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами авторизованного пользователя")
    public void createOrderWithIngredientsSuccessTest() {
        String token = createUser(email, password, name);
        DtoOrder request = new DtoOrder(ingredients);

        DtoIngredientsResponse ingredientsData = getIngredientsData();

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
                .body("order._id", notNullValue())
                .body("order.owner.name", equalTo(name))
                .body("order.owner.email", equalTo(email))
                .body("order.owner.createdAt", notNullValue())
                .body("order.owner.updatedAt", notNullValue())
                .body("order.status", equalTo("done"))
                .body("order.name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.createdAt", notNullValue())
                .body("order.updatedAt", notNullValue())
                .body("order.number", notNullValue())
                .body("order.price", equalTo(2325));

        //Проверка блока ингредиентов
        DtoOrderResponse createOrderIngredients = response.getBody().as(DtoOrderResponse.class);
        ingredientsData.data.toString().equals(createOrderIngredients.order.ingredients.toString());

        int orderNumber = getUserOrders(token);
        assertEquals(createOrderIngredients.order.number, orderNumber);

        deleteUser(token);
    }

    @Test
    @DisplayName("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationSuccessTest() {
        DtoOrder request = new DtoOrder(ingredients);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/orders");

        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("createOrderResponseWithoutAuthorizationJsonScheme.json"))
                .body("success", equalTo(true))
                .body("name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Ошибка создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsErrorTest() {
        String token = createUser(email, password, name);
        DtoOrder request = new DtoOrder();

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/orders");

        response.then()
                .assertThat()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка создания заказа неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientHashErrorTest() {
        String token = createUser(email, password, name);
        DtoOrder request = new DtoOrder();
        request.setIngredient("incorrect1hash");

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/orders")
                .then()
                .statusCode(500);

        deleteUser(token);
    }
}
