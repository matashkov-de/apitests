
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
public class DemoWebShopSearchTest {

    @Test
    void searchForTcpInstructorLedTraining() {
        //Вводим в строке поиска TCP Instructor Led Training
        //Проверяем, что предмет найден в поиске

        given()
                .when()
                .get("http://demowebshop.tricentis.com/catalog/searchtermautocomplete?term=TCP Instructor Led Training")
                .then()
                .log().all()
                .statusCode(200)
                .body("label", hasItem("TCP Instructor Led Training"));

    }

    @Test
    void addItemToWishList () {
        //Переходим к предмету и добавляем его в Wishlist
        //Проверяем, что предмет добавился в список Wishlist

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/66/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", notNullValue());
    }
}
