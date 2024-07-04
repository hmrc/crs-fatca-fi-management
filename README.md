
# crs-fatca-fi-management

This is the CRS/FATCA backend service for managing financial institutions 

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), and requires a Java 11 [JRE] to run.

### Running the service

This microservice is part of Service Manager: CRS_FATCA_ALL
or
Can be run individually through Service Manager: CRS_FATCA_FI_MANAGEMENT
Port: 10034

Link: http://localhost:10034/crs-fatca-fi-management

---

## Running Tests
Run unit tests:
```
sbt test
```

Run integration tests:
```
sbt "project it" test
```

---

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").