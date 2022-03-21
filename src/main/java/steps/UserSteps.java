package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.Utils.BASE_URL;

public class UserSteps {

    @Step("Получение данных о пользователе")
    public static Object getUserData(String token) {
        Response response = given()
                .auth().oauth2(token)
                .when()
                .get(BASE_URL + "/auth/user");

        response.then()
                .log().body()
                .statusCode(200);

        return response.jsonPath();
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String token) {
        given()
                .auth().oauth2(token)
                .when()
                .delete(BASE_URL + "/auth/user")
                .then()
                .statusCode(202);
    }
}
