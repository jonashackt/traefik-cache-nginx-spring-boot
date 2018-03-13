package de.jonashackt;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = WeatherclientApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"server.port=8087"}
)
public class WeatherclientTest {

	@Ignore
	@Test public void
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
