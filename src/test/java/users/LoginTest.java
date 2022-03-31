package users;

import dto.DtoUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserSteps.*;

public class LoginTest {

    String email = "user40@ya.ru";
    String password = "pass123";
    String name = "Naruto";
    String token;

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void loginSuccessTest() {
        createUser(new DtoUser(email, password, name));

        Response response = login(new DtoUser(email, password));
        response.then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("createUserResponseJsonScheme.json"))
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        token = response.then()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Ошибка авторизации при некорректном email")
    public void loginIncorrectEmailErrorTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");

        Response response = login(new DtoUser("incorrect_email", password));
        response.then()
                .assertThat()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка авторизации при некорректном password")
    public void loginIncorrectPasswordErrorTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");

        Response response = login(new DtoUser(email, "incorrect_password"));
        response.then()
                .assertThat()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка авторизации без email")
    public void loginWithoutEmailErrorTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");
        DtoUser dtoUserLogin = new DtoUser();
        dtoUserLogin.setPassword(password);

        Response response = login(dtoUserLogin);
        response.then()
                .assertThat()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка авторизации без password")
    public void loginWithoutPasswordErrorTest() {
        token = createUser(new DtoUser(email, password, name)).then().extract().path("accessToken");
        DtoUser dtoUserLogin = new DtoUser();
        dtoUserLogin.setEmail(email);

        Response response = login(dtoUserLogin);
        response.then()
                .assertThat()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("errorJsonScheme.json"))
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser(token);
            token = null;
        }
    }
}
