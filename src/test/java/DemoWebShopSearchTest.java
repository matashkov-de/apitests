
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.RegCredentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
public class DemoWebShopSearchTest {

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void registrationSuccessfulTest() {
        //request: https://reqres.in/api/register
        //check: Статус 200
        //check: Вернулся не пустой token

        RegCredentials credentials = new RegCredentials();

        credentials.setEmail("eve.holt@reqres.in");
        credentials.setPassword("pistol");

        given()
                .log().uri()
                .log().body()
                .filter(withCustomTemplates())
                .body(credentials)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    void searchForTcpInstructorLedTraining() {
        //Вводим в строке поиска TCP Instructor Led Training
        //Проверяем, что предмет найден в поиске

        given()
                .log().uri()
                .log().body()
                .filter(withCustomTemplates())
                .when()
                .get("http://demowebshop.tricentis.com/catalog/searchtermautocomplete?term=TCP Instructor Led Training")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("label", hasItem("TCP Instructor Led Training"));

    }

    @Test
    void addItemToWishList () {
        //Переходим к предмету и добавляем его в Wishlist
        //Проверяем, что предмет добавился в список Wishlist
        //

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .log().uri()
                .log().body()
                .filter(withCustomTemplates())
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/66/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/Demowebshop_addToWishlist_Response.json"))
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", notNullValue());
    }
}
