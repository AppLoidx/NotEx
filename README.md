# NotEx (in development)

![](https://img.shields.io/badge/PMD-3.13.0-GREEN?style=for-the-badge)
![](https://img.shields.io/badge/Java-8-2787F5?style=for-the-badge)
![](https://img.shields.io/badge/version-0.0.1-19191A?style=for-the-badge)
<hr>

![](assets/Not_Ex-Logo.jpg)

Say "no" to exceptions

## Dependency

```xml
<dependency>
  <groupId>com.apploidxxx</groupId>
  <artifactId>notex</artifactId>
  <version>0.0.1-ALPHA</version>
</dependency>
```

## Basic example

Basic idea of this library is to use notifications instead of throwing exceptions

````java
@Test
public void basicResultUsage() {
    Boolean resolve = Result.of("Hey!!")
            .resolveFrom(String::length)
            .resolve(this::assertOdd)
            .resolve(this::assertTrue)
            .resolve(val -> val, notification -> {
                log.info(notification.getMessage());
                return false;
            });

    log.info(resolve.toString());
}

private <T> Result<Boolean, T> assertOdd(int number) {
    if (number % 2 == 0) {
        return Result.of(Notification.of("Number is not odd at all!"));
    } else {
        return Result.of(true);
    }
}

private <S> Result<Boolean, S> assertTrue(Boolean bool) {
    if (bool) {
        return Result.of(true);
    } else {
        return Result.of(Notification.of("This result contains error!"));

    }
}
````

## Documentation
* [Project home page (current)](http://apploidx.github.io/NotEx/)
* [Project](http://apploidx.github.io/NotEx/docs/)
* [JavaDoc](http://apploidx.github.io/NotEx/docs/apidocs/com/apploidxxx/notex/package-summary.html)

## References

* Martin Fowler, [Replacing Throwing Exceptions with Notification in Validations](https://martinfowler.com/articles/replaceThrowWithNotification.html)
    * If you're validating some data, you usually shouldn't be using exceptions to signal validation failures. Here I describe how I'd refactor such code into using the Notification pattern.
