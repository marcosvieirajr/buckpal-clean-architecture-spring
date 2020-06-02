[![CircleCI](https://circleci.com/gh/marcosvieirajr/buckpal-clean-architecture-spring.svg?style=svg)](https://circleci.com/gh/marcosvieirajr/buckpal-clean-architecture-spring)

# Buckpal

An example approach for implementating a Clean/Hexagonal Architecture based the book: "Get Your Hands Dirty on Clean Architecture", by Tom Hombergs

## Dependencies

- [x] java 11
- [x] spring boot 2.3.0
    - [x] data
    - [x] web
- [x] lombok
- [x] flywaydb
- [x] postgres
- [x] modelmapper
- [x] assertj

## Todo

- [x] rich domain
    - [x] unit tests
- [x] input ports
    - [x] self validator input ports commands
- [x] output ports
- [x] use case (implements input ports)
    - [x] unit tests
- [x] web api adapters (use input ports)
    - [x] integration tests
    - [x] exception handlers
    - [ ] spring data pagination & sort
    - [ ] spring hateoas
    - [ ] swagger
- [x] persistence adapters (implements output ports)
    - [x] jpa entities
    - [x] repositories
    - [ ] integration tests
- [ ] system tests (end to end)
- [ ] redis cache
- [ ] split in multi modules
- [ ] split in microservices
    - [ ] spring cloud NETFLIX OSS
        - [ ] netflix EUREKA - service discovery
        - [ ] netflix ZULL - gateway router and filter
        - [ ] netflix FEIGN - declarative web service client
            - [ ] netflix HYSTRIX - fallback methods
        - [ ] cloud config - centralized configuration
        - [ ] cloud security - security configuration
- [ ] docker-compose stack
    - [x] postgres
    - [x] mongo
    - [ ] redis
- [ ] ci / cd






