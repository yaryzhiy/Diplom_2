package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static steps.UserSteps.*;
import static utils.Utils.BASE_URL;

public class UpdateUserDataTest {

    @Test
    @DisplayName("Успешное обновление информации о пользователе")
    public void updateUserDataSuccessTest() {
        String email = "usUpdate@ya.ru";
        String password = "pass123Update";
        String name = "NarutoUpdate";
        String token = createUser("us@ya.ru", "pass123", "Naruto");
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
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));

        DtoUser user = getUserData(token);
        assertEquals(user.email, equalTo(email.toLowerCase()));
        assertEquals(user.name, equalTo(name));

        login(email, password);
        deleteUser(token);
    }
}
