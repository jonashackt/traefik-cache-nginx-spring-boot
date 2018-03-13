traefik-cache-nginx-spring-boot 
=============================
[![Build Status](https://travis-ci.org/jonashackt/traefik-cache-nginx-spring-boot.svg?branch=master)](https://travis-ci.org/jonashackt/traefik-cache-nginx-spring-boot)

As [Traefik](https://traefik.io/) is a really gread & modern loadbalancer, but it sadly [doesn´t feature caching right now](https://github.com/containous/traefik/issues/878). So we need to put something in front of it, that is able to do caching - like old [Nginx](https://nginx.org/en/).

And as I like full examples, let´s bring in a client application (weatherclient), which want´s to call a server backend (weatherbackend). Both are implemented as simple Spring Boot microservices, as the following ASCII shows: 

```
                  -----------------------------------------------------------------------------    
                 | Docker Network scope                                                        |  
                 |                                                                             |  
                 |                                                                             |   
                 |                                                                             |
 ============    |   ==============     ================     ===============     ============  |
 =  docker- =    |   =            =     =              =     =             =     =          =  |
 = network- = -----> =   weather  = --> =    Nginx     = --> =   Traefik   = --> =  weather =  |
 =  client  =    |   =    client  =     =  (caching)   =     = (loadbalan.)=     =  backend =  |
 ============    |   ==============     ================     ===============     ============  |
                 |                                                                             |
                 |                                                                             |
                 |                                                                             |
                  -----------------------------------------------------------------------------
                 
```

It also shows, that we simulate the whole scenario with [Docker](https://www.docker.com/). To have the chance to execute everything within an intergration test, we use [docker-compose-rule](https://github.com/palantir/docker-compose-rule) and the docker-network-client app. Why?

As the weatherclient only has access to the DNS alias `weatherbackend`, if it itself is part of the Docker (Compose) network, we need another way to run an Integration test inside the Docker network scope. Therefore we use the [docker-compose-rule](https://github.com/palantir/docker-compose-rule) and the __docker-network-client__ that just calls __weatherclient__ inside the Docker network.




# HowTo Use

Everything you need to run a full build and __complete__ test (incl. Integrationtest of docker-network-client firing up all microservices that´ll call each other with client certificate support) is this:

```
mvn clean install
```

Only, if you want to check manually, you can do a `docker-compose up -d` and open your Browser with [http:localhost:8080/swagger-ui.html] and fire up a GET-Request to /secretservers with Swagger :)

