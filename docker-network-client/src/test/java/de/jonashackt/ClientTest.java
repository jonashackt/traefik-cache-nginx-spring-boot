package de.jonashackt;

import org.apache.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class ClientTest {

	@ClassRule
	public static DockerComposeContainer services =
			new DockerComposeContainer(new File("../docker-compose.yml"))
					.withExposedService("weatherbackend", 8095, Wait.forListeningPort())
					.withExposedService("traefik", 8080, Wait.forListeningPort())
					.withExposedService("nginx", 80, Wait.forListeningPort())
					.withExposedService("weatherclient", 8085, Wait.forHttp("/swagger-ui.html").forStatusCode(200));


	@Test
	public void is_weatherclient_able_to_call_weatherbackend_through_nginx_and_traefik() {
		given()
				.pathParam("cityname", "Weimar")
		.when()
			.get("http://localhost:8085/forecast/{cityname}")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat()
				.body(containsString("Hello Weimar! This is a RESTful HttpService written in Spring."));
	}
}
