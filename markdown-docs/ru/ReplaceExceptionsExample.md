# Примеры замены исключений (в работе)


Из исходного кода vk-java-sdk:
```java
try {
    T result = gson.fromJson(response, responseClass);
    if (result instanceof Validable) {
        try {
            Validable validable = (Validable) result;
            validable.validateRequired();
        } catch (RequiredFieldException e) {
            throw new ClientException("JSON validate fail: " + textResponse + "\n" + e.toString());
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new ClientException("JSON validate fail:" + textResponse + e.toString());
        }
    }

    return result;
} catch (JsonSyntaxException e) {
    throw new ClientException("Can't parse json response: " + textResponse + "\n" + e.toString());
}
```
После изменений (с учетом того, что библиотека не поддерживает NotEx)
```java

return wrapException(() -> gson.fromJson(response, responseClass), 
        e -> "Can't parse json response: " + textResponse + "\n" + e.toString())
        .apply(res -> {
            if (res instanceof Validable) {
                Validable validable = (Validable) res;
                return wrapException(() -> validable.validateRequired(), 
                    e -> "JSON validate fail: " + textResponse + "\n" + e.toString())
            }
        });
```

Если бы библиотека поддерживала NotEx

```java
gson.fromJson(response, responseClass)
        .apply(res -> {
            if (res instanceof Validable) {
                Validable validable = (Validable) res;
                return validable.validateRequired();
            }
        })
        .resolve(e -> {
            e -> "Can't parse json response: " + e.getErrorMessage();
        });
```


<hr/>

В следующем примере можно увидеть сходства с библиотекой. По сути обработка ошибки происходит в `catch` блоке и 
дальше после `try-catch` блока. Здесь вместо нотификации используется `ApiCaptchaException`

```java
String captchaSid = null;
String captchaImg = null;

try {
    vk.wall().post(actor).message("Hello world").execute();
} catch (ApiCaptchaException e) {
    captchaSid = e.getCaptchaSid();
    captchaImg = e.getCaptchaImg();
}

//Showing captcha image...

if (captchaImg != null) {
    vk.wall().post(actor)
        .message("Hello world")
        .captchaSid(captchaSid)
        .captchaKey(captchaKey)
        .execute();
}
```
При условии, что `execute` бросает исключение

```java
wrapException(() -> vk.wall().post(actor).message("Hello world").execute(),
        e -> {
            if (e.getCaptchaImg() != null) {
                vk.wall().post(actor)
                    .message("Hello world")
                    .captchaSid(e.getCaptchaSid())
                    .captchaKey(e.getCaptchaKey())
                    .execute();
            }
        })
```

При полной интеграции

```java
vk.wall().post(actor).message("Hello world").execute()
        .resolve(e -> {
                if (e.getCaptchaImg() != null) {
                    vk.wall().post(actor)
                        .message("Hello world")
                        .captchaSid(e.getCaptchaSid())
                        .captchaKey(e.getCaptchaKey())
                        .execute();
                }
        })
```

Причем в такой конструкции явно видно, где стоит обернуть код в отдельный метод, что
повысит читабельность кода:

```java
vk.wall().post(actor).message("Hello world").execute()
        .resolve(this::executeResolve)

// ...

void executeResolve(ExecuteError e) {
    if (e.getCaptchaImg() != null) {
        vk.wall().post(actor)
            .message("Hello world")
            .captchaSid(e.getCaptchaSid())
            .captchaKey(e.getCaptchaKey())
            .execute();
    }
}
```

Здесь мы также сталкиваемся с паттерном использования выбрасывания исключения как
о результате выполнения операции. В случае, если такая операция прерывается ошибкой,
которая не входит в бизнес логику, например, потеря соединения, то использование исключения
оправдано. В другом случае, когда результат операции может быть неоднозначным, то есть не только
`"успех"`/`"ошибка"`, а, например, необходимость дополнительной проверки через Captcha, то
использование исключения избыточно и приводит к сложности чтения и обработки кода.

Для этого следует использовать не `Result`, а `VoidResult`, который не содержит в себе 
возвращаемого объекта, а лишь статус ошибки и его описание

```java
public class VoidResult<S>
```

