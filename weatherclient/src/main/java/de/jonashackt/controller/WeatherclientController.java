package de.jonashackt.controller;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@RestController
public class WeatherclientController {

    private RestTemplate restTemplate = createRestTemplateWithNginxProxy();

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

        //return restTemplate.getForObject("http://weatherbackend.server.test:80/weather/" + cityname, String.class);
    }

    private RestTemplate createRestTemplateWithNginxProxy() {
        return new RestTemplate();
    }

    private SimpleClientHttpRequestFactory createNginxRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setProxy(createNginxProxy());
        return simpleClientHttpRequestFactory;
    }

    private Proxy createNginxProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress("nginx", 80));
    }
}
