package steps;

import dto.DtoUser;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class UserSteps {

    @Step("Создание пользователя")
    public static Response createUser(DtoUser dtoUser) {
        return given()
                .header("Content-type", "application/json")
                .body(dtoUser)
                .when()
                .post(BASE_URL + "/auth/register");
    }

    @Step("Авторизация пользователя")
    public static Response login(DtoUser dtoUser) {
        return given()
                .header("Content-type", "application/json")
                .body(dtoUser)
                .when()
                .post(BASE_URL + "/auth/login");
    }

    @Step("Получение данных о пользователе")
    public static Response getUserData(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + "/auth/user");
    }

    @Step("Обновление информации о пользователе")
    public static Response updateUserData(DtoUser dtoUserUpdate, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(dtoUserUpdate)
                .when()
                .patch(BASE_URL + "/auth/user");
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
