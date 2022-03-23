package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;
import static utils.Utils.BASE_URL;

public class LoginTest {

    String email = "user10@ya.ru";
    String password = "pass123";
    String name = "Naruto";

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void loginSuccessTest() {
        createUser(email, password, name);
        DtoUser request = new DtoUser(email, password);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body(matchesJsonSchemaInClasspath("createOrderResponseJsonScheme.json"))
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        String token = response.then()
                .extract()
                .path("accessToken");

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка авторизации при некорректном email")
    public void loginIncorrectEmailErrorTest() {
        String token = createUser(email, password, name);
        DtoUser request = new DtoUser("incorrect_email", password);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка авторизации при некорректном password")
    public void loginIncorrectPasswordErrorTest() {
        String token = createUser(email, password, name);
        DtoUser request = new DtoUser(email, "incorrect_password");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка авторизации без email")
    public void loginWithoutEmailErrorTest() {
        String token = createUser(email, password, name);
        DtoUser request = new DtoUser();
        request.setPassword(password);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка авторизации без password")
    public void loginWithoutPasswordErrorTest() {
        String token = createUser(email, password, name);
        DtoUser request = new DtoUser();
        request.setEmail(email);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        deleteUser(token);
    }
}
