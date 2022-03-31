package orders;

import dto.DtoOrderRequest;
import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.OrderSteps.*;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

public class CreateOrderTest {

    String email = "user40@ya.ru";
    String password = "pass123";
    String name = "Naruto";
    public ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    String token;

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами авторизованного пользователя")
    public void createOrderWithIngredientsSuccessTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");

        Response ingredientsData = getIngredients();

        Response response = createOrderByAuthUser(new DtoOrderRequest(ingredients), token);
        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("createOrderResponseJsonScheme.json"))
                .body("success", equalTo(true))
                .body("name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.ingredients[0]", equalTo(ingredientsData.then().extract().path("data[0]")))
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

        getUserOrders(token).then()
                .statusCode(200)
                .body("orders[0].number", equalTo(response.then().extract().path("order.number")));
    }

    @Test
    @DisplayName("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationSuccessTest() {
        Response response = createOrderByUnauthUser(new DtoOrderRequest(ingredients));
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
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");

        Response response = createOrderByAuthUser(new DtoOrderRequest(), token);
        response.then()
                .assertThat()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Ошибка создания заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientHashErrorTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");
        DtoOrderRequest dtoOrderRequest = new DtoOrderRequest();
        dtoOrderRequest.setIngredient("incorrect1hash");

        createOrderByAuthUser(dtoOrderRequest, token)
                .then()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser(token);
            token = null;
        }
    }
}
