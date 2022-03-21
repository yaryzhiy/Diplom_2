package users;

import dto.DtoUser;
import dto.DtoUserData;
import dto.DtoUserResponse;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserSteps.deleteUser;
import static steps.UserSteps.getUserData;
import static utils.Utils.BASE_URL;
import static utils.Utils.mapper;

public class CreateUserTest {

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUserSuccessTest() throws IOException {
        String email = "userr@ya.ru";
        String password = "pass123";
        String name = "Naruto";
        DtoUser request = new DtoUser(email, password, name);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .log().body()
                .body(request)
                .when()
                .post(BASE_URL + "/auth/register");

        response.then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .and()
                .body(matchesJsonSchemaInClasspath("createOrderResponseJsonScheme.json"))
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

        DtoUserResponse dtoResponse = mapper(response.jsonPath());
        Object conf = getUserData(dtoResponse.accessToken);

        deleteUser(dtoResponse.accessToken);
    }
}
