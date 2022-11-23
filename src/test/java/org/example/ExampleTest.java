package org.example;


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
public class ExampleTest extends AbstractTest {
    @Test
    void getExampleTest() {
        given()
                .when()
                .get(getBaseUrl()+"recipes/716429/information?" +
                        "includeNutrition=false&apiKey=" + getApiKey()) //переход на страницу
                .then()
                .statusCode(200); //ассерт
    }
    @Test
    void getDataTest() {
        given()
                .queryParam("apiKey", getApiKey()) // тоже самое, только ссылка разбита на отдельные параметры
                .queryParam("includeNutrition", "false")
                .pathParam("id", 716429) // можно через переменные
                .when()
                .get(getBaseUrl() + "recipes/{id}/information")
                .then()
                .statusCode(200);
    }
    @Test
    void postDataTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded") //доп.параметры для пост-запроса (из доков)
                .formParam("title","Pork roast with green beans")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")// пост-запрос
                .then()
                .statusCode(200);
        given().cookie("username", "Alexandr")//вариант теста с передачей куки (параметр=значение)
                .when()
                .get(getBaseUrl()+"recipes/716429/information?" +
                        "includeNutrition=false&apiKey=" + getApiKey()) //переход на страницу
                .then()
                .statusCode(200); //ассерт
        given()// а здесь пост-запрос с боди: джейсон прописанный строкой
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
                .post(getBaseUrl()+"mealplanner/geekbrains/items")
                .then()
                .statusCode(200);
    }
    @Test
    void getResponseData() { //что можно вытащить из ответа в консоль
        Response response = given()
                .when()
                .get(getBaseUrl() + "recipes/716429/information?" +
                        "includeNutrition=false&apiKey=" + getApiKey());

        // Get all headers
        Headers allHeaders = response.getHeaders();
        // Get a single header value:
        System.out.println("Content-Encoding: " + response.getHeader("Content-Encoding"));
        // Get all cookies as simple name-value pairs

        Map<String, String> allCookies = response.getCookies();
        // Get a single cookie value:
        System.out.println("CookieName: " + response.getCookie("cookieName"));

        String cuisine = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .path("cuisine");

        System.out.println("cuisine: " + cuisine);
     }
    @Test
    void getVerifyingResponseData() { // с ассертами

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .body()
                .jsonPath();
        assertThat(response.get("vegetarian"), is(false));
        assertThat(response.get("vegan"), is(false));
        assertThat(response.get("license"), equalTo("CC BY-SA 3.0"));
        assertThat(response.get("pricePerServing"), equalTo(163.15F));
        assertThat(response.get("extendedIngredients[0].aisle"), equalTo("Milk, Eggs, Other Dairy"));

        given() //проверки по заголовкам
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .then()
                .assertThat()
                //.cookie("cookieName", "cookieValue")
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .statusLine(containsString("OK"))
                .header("Connection", "keep-alive")
                .header("Content-Length", Integer::parseInt, lessThan(3000))
                .contentType(ContentType.JSON)
                //  .body(equalTo("something"))
                .time(lessThan(2000L));

        given() // проверки по боди
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .response()
                .contentType(ContentType.JSON)
                .time(lessThan(2000L))
                .header("Connection", "keep-alive")
                .expect()
                .body("vegetarian", is(false))
                .body("vegan", is(false))
                .body("license", equalTo("CC BY-SA 3.0"))
                .body("pricePerServing", equalTo(163.15F))
                .body("extendedIngredients[0].aisle", equalTo("Milk, Eggs, Other Dairy"))
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information");
    }
    @Test
    void getRequestLogTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .log().method() //логируем отдельные части методом Лог
                .log().params()
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information");

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++=");

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .log().all() //или логируем все
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .prettyPeek(); // и логируем ответ
    }
    //@BeforeAll
    //static void setUp(){
        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); - логирует запрос и ответ в случае
    } // ошибки, лог.олл писать не нужно, это отдельный метод для всех тестов
