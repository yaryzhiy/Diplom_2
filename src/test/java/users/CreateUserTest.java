package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserSteps.*;
import static utils.Utils.BASE_URL;

public class CreateUserTest {

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest() {
        String email = "us@ya.ru";
        String password = "pass123";
        String name = "Naruto";
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

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

        getUserData(token);
        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка при создании уже зарегистрированного пользователя")
    public void createRegisteredUserErrorTest() {
        String email = "us@ya.ru";
        String password = "pass123";
        String name = "Naruto";
        String token = createUser(email, password, name);
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

        deleteUser(token);
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без email")
    public void createUserWithoutEmailErrorTest() {
        DtoUser request = new DtoUser();
        request.setPassword("pass123");
        request.setName("Naruto");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без password")
    public void createUserWithoutPasswordErrorTest() {
        DtoUser request = new DtoUser();
        request.setEmail("us@ya.ru");
        request.setName("Naruto");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без name")
    public void createUserWithoutNameErrorTest() {
        DtoUser request = new DtoUser();
        request.setEmail("us@ya.ru");
        request.setPassword("pass123");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .assertThat()
                .statusCode(403)
                .and()
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
