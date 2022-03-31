package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.*;
import static steps.UserSteps.*;
import static utils.Utils.BASE_URL;

public class UpdateUserDataTest {

    String email = "user40@ya.ru";
    String password = "pass123";
    String name = "Naruto";
    String emailUpdate = "user40update@ya.ru";
    String passwordUpdate = "pass123Update";
    String nameUpdate = "NarutoUpdate";
    String token;

    @Test
    @DisplayName("Успешное обновление информации о пользователе")
    public void updateUserDataSuccessTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");

        Response response = updateUserData(new DtoUser(emailUpdate, passwordUpdate, nameUpdate), token);
        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("userDataJsonScheme.json"))
                .body("success", equalTo(true))
                .body("user.email", equalTo(emailUpdate))
                .body("user.name", equalTo(nameUpdate));

        getUserData(token).then()
                .assertThat()
                .body("user.email", equalTo(emailUpdate))
                .body("user.name", equalTo(nameUpdate));

        login(new DtoUser(emailUpdate, passwordUpdate)).then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Ошибка обновления информации неавторизованного пользователя")
    public void updateUnauthorizedUserDataErrorTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(new DtoUser(emailUpdate, passwordUpdate, nameUpdate))
                .when()
                .patch(BASE_URL + "/auth/user");

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
