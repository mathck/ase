package at.tuwien.ase.integration.rest;

import static com.jayway.restassured.RestAssured.given;

import at.tuwien.ase.model.User;
import com.jayway.restassured.path.json.JsonPath;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

// @author: Mateusz Czernecki
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class UserTests {

    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "taskit/api/user";
    }

    @Before
    public void setup() throws Exception {
        requestSpecBuilder.setContentType(ContentType.JSON).addHeader("Accept", ContentType.JSON.getAcceptHeader());
    }

    // role tests todo ex. delete something or create something i cannot do

    @Test
    public void RegisterReturns200() {
        // Arrange
        ensureUserDeleted();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("firstName", getSampleUser().getFirstName());
        map.put("lastName", getSampleUser().getLastName());
        map.put("avatar", getSampleUser().getAvatar());
        map.put("userID", getSampleUser().getUserID());
        map.put("password", PASSWORD);

        // Act + Assert
        given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("/register")
                .then()
                .statusCode(200);
    }

    @Test
    public void LoginReturnsToken() {
        // Arrange

        // Act
        JsonPath response = given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .param("email", getSampleUser().getUserID())
                .param("password", PASSWORD)
                .when()
                .get("/login")
                .then()
                .statusCode(200).extract().body().jsonPath();

        // Assert
        String resp = response.getJsonObject("token");
        Assert.assertNotNull(resp);
    }

    @Test
    public void DeleteUser200() {
        // Arrange
        ensrureUserExists();
        String token = ensureLoggedIn();

        // Act + Assert
        JsonPath response = given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .header("user-token", token)
                .param("uID", getSampleUser().getUserID())
                .when()
                .delete("")
                .then()
                .statusCode(200).extract().body().jsonPath();
    }

    @Test
    public void correctLogoutReturns200() {
        // Arrange
        ensrureUserExists();
        String token = ensureLoggedIn();

        // Act
        given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .header("user-token", token)
                .param("email", getSampleUser().getUserID())
                .when()
                .patch("/logout")
                .then()
                .statusCode(200);

        // Assert
    }

    @Test
    public void LogoutWithoutuIDReturns401() {
        // Arrange

        // Act
        given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .when()
                .patch("/logout")
                .then()
                .statusCode(401);

        // Assert
    }

    private User getSampleUser() {
        User user = new User("testuser@mail.com", PASSWORD);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAvatar(";)");

        return user;
    }

    private static String PASSWORD = "1234qwer";

    private void ensureUserDeleted() {
        // Arrange
        ensrureUserExists();
        String token = ensureLoggedIn();

        // Act + Assert
        JsonPath response = given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .header("user-token", token)
                .param("uID", getSampleUser().getUserID())
                .when()
                .delete("")
                .then()
                .statusCode(200).extract().body().jsonPath();
    }

    private void ensrureUserExists() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("firstName", getSampleUser().getFirstName());
        map.put("lastName", getSampleUser().getLastName());
        map.put("avatar", getSampleUser().getAvatar());
        map.put("userID", getSampleUser().getUserID());
        map.put("password", PASSWORD);

        // Act + Assert
        given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post("/register");
    }

    private String ensureLoggedIn() {
        return given()
                .spec(requestSpecBuilder.build())
                .log().all()
                .param("email", getSampleUser().getUserID())
                .param("password", PASSWORD)
                .when()
                .get("/login")
                .then()
                .statusCode(200).extract().body().jsonPath().getJsonObject("token");
    }
}
