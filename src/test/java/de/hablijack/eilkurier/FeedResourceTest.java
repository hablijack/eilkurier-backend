package de.hablijack.eilkurier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FeedResourceTest {

  @Test
  public void testHelloEndpoint() {
    given()
        .when().get("/api")
        .then()
        .statusCode(200)
        .body(is("Hello RESTEasy"));
  }

}