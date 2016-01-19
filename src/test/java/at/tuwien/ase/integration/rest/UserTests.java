package at.tuwien.ase.integration.rest;

import static com.jayway.restassured.RestAssured.given;

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
    public void logoutWithoutTokenReturns401() {
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
}
