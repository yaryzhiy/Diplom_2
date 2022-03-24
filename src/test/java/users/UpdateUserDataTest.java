package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static steps.UserSteps.*;
import static utils.Utils.BASE_URL;

public class UpdateUserDataTest {

    String email = "user33update@ya.ru";
    String password = "pass123Update";
    String name = "NarutoUpdate";

    @Test
    @DisplayName("Успешное обновление информации о пользователе")
    public void updateUserDataSuccessTest() {
        String token = createUser("user33@ya.ru", "pass123", "Naruto");
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .patch(BASE_URL + "/auth/user");

        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body(matchesJsonSchemaInClasspath("userDataJsonScheme.json"))
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));

        DtoUser user = getUserData(token);
        assertEquals(user.email, email);
        assertEquals(user.name, name);

        login(email, password);
        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка обновления информации неавторизованного пользователя")
    public void updateUnauthorizedUserDataErrorTest() {
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .patch(BASE_URL + "/auth/user");

        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
