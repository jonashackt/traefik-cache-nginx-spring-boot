package de.jonashackt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherclientController {

    @GetMapping("/forecast/{cityname}")
    @ResponseStatus(HttpStatus.OK)
    public String forecast(@PathVariable("cityname") String cityname) {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject("http://weatherbackend.server.test:80/weather/" + cityname, String.class);
    }
}
