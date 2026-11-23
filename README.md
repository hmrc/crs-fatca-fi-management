
# crs-fatca-fi-management

This is the CRS/FATCA backend service for managing financial institutions 

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), and requires a Java 21 [JRE] to run.

### Running the service

**Service manager:** CRS_FATCA_ALL

**Individually via service manager:** CRS_FATCA_FI_MANAGEMENT

**Port:** 10034

Link: http://localhost:10034/crs-fatca-fi-management

### API

| Task                             | Supported methods | Description                                  |
|----------------------------------|-------------------|----------------------------------------------|
| /financial-institutions/create   | POST              | Create a Financial Institution               |
| /financial-institutions/update | POST              | Update a existing Financial Institution      |
| /financial-institutions/remove             | POST              | Remove a existing Financial Institution      |
| /financial-institutions/:subscriptionId               | GET               | Returns list of FI for user (SubscriptionId) |
| /financial-institutions/:subscriptionId/:financialInstitutionId               | GET               | Returns Detail of Financial Institution      |


---

## Running Tests
Run unit tests:
```
sbt test
```

Run integration tests:
```
sbt it/test
```

---

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").