package de.jonashackt;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.apache.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class ClientTest {

	@ClassRule
	public static DockerComposeRule docker = DockerComposeRule.builder()
			.file("../docker-compose.yml")
			.waitingForService("weatherbackend", HealthChecks.toHaveAllPortsOpen())
			.waitingForService("traefik", HealthChecks.toHaveAllPortsOpen())
			.waitingForService("nginx", HealthChecks.toHaveAllPortsOpen())
			.waitingForService("weatherclient",  HealthChecks.toRespondOverHttp(8080, (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT/swagger-ui.html")))
			.build();

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
