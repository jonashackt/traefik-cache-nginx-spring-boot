package de.jonashackt;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(
		classes = WeatherclientApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"server.port=8087"}
)
public class WeatherclientTest {

	@Disabled
	@Test
	public void
	should_retrieve_simple_response_form_weatherbackend() {

		given()
			.contentType(ContentType.JSON)
			.pathParam("cityname", "Weimar")
		.when()
			.get("http://localhost:8087/forecast/{cityname}")
		.then()
			.statusCode(HttpStatus.SC_OK);
	}

}
