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

### Nginx + Traefik + weatherbackend in logical scope (aka host) with the help of Docker DNS

Additionally, in real world scenarios, Nginx + Traefik + weatherbackend would reside on a separate host with their own DNS configuration. So there´s a second "logical" scope here, which we could have implemented with tools like Vagrant - but this would have been overkill here.

Trying to imitate a machine, where Traefik + weatherbackend + Nginx are running all on one machine with DNS configured, we configure the [Docker DNS alias](https://docs.docker.com/v17.09/engine/userguide/networking/configure-dns/) for weatherbackend to the Traefik container, which routes it then to the weatherbackend. This is done with the help of this Docker Compose configuration ([see the docs](https://docs.docker.com/compose/compose-file/#aliases)):

```
  traefik:
    ...
    networks:
      default:
        aliases:
          - weatherbackend.server.test
    ...
```

Instead of configuring the weatherbackend directly to have the DNS alias `weatherbackend.server.test`, we use the Traefik Docker Compose service here - which has a similar effect to a scoped machine around Nginx, Traefik & weatherbackend. We should see the call now in Traefik GUI:

![first-call-weatherbackend-through-traefik-with-docker-dns-configured](first-call-weatherbackend-through-traefik-with-docker-dns-configured.png)



# HowTo Use

Everything you need to run a full build and __complete__ test (incl. Integrationtest of docker-network-client firing up all microservices that´ll call each other with client certificate support) is this:

```
mvn clean install
docker-compose up -d
```

Now you can have a look at some of the components of our architecture:

Traefik: http://localhost:8080/dashboard/#/

weatherclient: http://localhost:8085/swagger-ui.html

Nginx: http://localhost:8088/

