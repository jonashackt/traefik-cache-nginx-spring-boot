package de.jonashackt.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@RestController
public class WeatherclientController {

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * Without the System property, we won´t be able to set the Host header, see
     * https://stackoverflow.com/questions/43223261/setting-host-header-for-spring-resttemplate-doesnt-work/43224279
     * and https://stackoverflow.com/a/8172736/4964553
     */
    @PostConstruct
    public void setProperty() {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    @GetMapping("/forecast/{cityname}")
    @ResponseStatus(HttpStatus.OK)
    public String forecast(@PathVariable("cityname") String cityname) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", "weatherbackend.server.test");

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://nginx:80/weather/" + cityname,
                HttpMethod.GET,
                new HttpEntity<String>(null, headers),
                String.class);

        return responseEntity.getBody();
    }
}
