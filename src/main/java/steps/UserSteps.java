package steps;

import dto.DtoUser;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class UserSteps {

    @Step("Создание пользователя")
    public static String createUser(String email, String password, String name) {
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .statusCode(200);

        String token = response.then()
                .extract()
                .path("accessToken");

        return token;
    }

    @Step("Авторизация пользователя")
    public static void login(String email, String password) {
        DtoUser dtoUser = new DtoUser(email, password);

        Response response = given()
                .header("Content-type", "application/json")
                .body(dtoUser)
                .when()
                .post(BASE_URL + "/auth/login");

        response.then()
                .statusCode(200);
    }

    @Step("Получение данных о пользователе")
    public static DtoUser getUserData(String token) {
        Response response = given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + "/auth/user");

        response.then()
                .log().body()
                .statusCode(200);

        DtoUser user = new DtoUser();
        user.setEmail(response.then().extract().path("user.email"));
        user.setName(response.then().extract().path("user.name"));
        return user;
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + "/auth/user")
                .then()
                .statusCode(202);
    }
}
