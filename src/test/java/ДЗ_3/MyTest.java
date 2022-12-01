package ДЗ_3;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import io.restassured.response.Response;
public class MyTest extends AbstractTest {
    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test
    void getBurger() {
         given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "burger")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200);
    }
    @Test
    void getPasta() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .expect()
                .body("totalResults", is(223))
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200);
    }
    @Test
    void getPizza() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pizza")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("number"), is(10));
    }
    @Test
    void getItalian() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine", "italian")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), is(262));
    }
    @Test
    void getWine() {
        given().spec(getRequestSpecification())
                .queryParam("query", "wine")
                .queryParam("minAlcohol", 8)
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);
    }
    @Test
    void postBurger() {
        given().spec(getRequestSpecification())
                .queryParam("query", "burger")
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);
    }
    @Test
    void postPizza() {
        given().spec(getRequestSpecification())
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pizza")
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);
    }
    @Test
    void postPasta() {
        given().spec(getRequestSpecification())
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(responseSpecification);
    }
    @Test
    void connectUser () { // не могу понять почему не проходит авторизация
        String body = given()
                .queryParam("apiKey", getApiKey())

                .body("{\n"
                        + " \"username\": \"Alex68\",\n"
                        + " \"firstName\": \"Alexandr\",\n"
                        + " \"lastName\": \"Butusov\",\n"
                        + " \"email\": \"boo2zz75384@mail.ru\" \n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/users/connect")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("hash")

                .toString();
        System.out.println("hash: " + body);
        String id = given()
                .queryParam("hash", "51ec966754e34158046721a8e65d2e3c4ea54c53")
                .pathParam("username", "Alex68")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 1589500800,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/{username}/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }
    @Test
    void addMealTest() {
        String id = given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/geekbrains/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given().spec(getRequestSpecification())
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .delete("https://api.spoonacular.com/mealplanner/geekbrains/items/" + id)
                .then()
                .spec(responseSpecification);
    }
}
