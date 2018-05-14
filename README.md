# juridisklogg-rest-client

Simple client library for archiving messages to the new "Juridisk Logg" using its REST API.

## Basic Usage

#### Add dependency:

```xml
<dependency>
    <groupId>no.nav.common</groupId>
    <artifactId>juridisklogg-rest-client</artifactId>
    <version>1.0.3</version>
</dependency>
```

#### Instantiate an `ArchiveRequest`
Java:
```java
ArchiveReqeuest request = new ArchiveRequest.Builder();
    .setMessageId("123-abc")                    // String
    .setMessageContent("hello".getBytes())      // byte[]
    .setSender("sender")                        // String
    .setReceiver("receiver")                    // String
    .setJoarkRef("ref")                         // String, optional
    .setRetention(10)                           // Int, optional, default is 10
    .build();
```

Kotlin (you could use the builder here too... but Kotlin has named arguments):
```kotlin
val request = ArchiveRequest(
    messageId = "123-abc",                      // String
    messageContent = "hello".tobyteArray(),     // ByteArray
    sender = "sender",                          // String
    receiver = "receiver",                      // String
    joarkReference = "ref",                     // String, optional
    retentionInYears = 10                       // Int, optional, default is 10
)
```

#### Instantiate the `RestArchiver`
Java:
```java
RestArchiver archiver = new RestArchiver(
    "username",                 // String, username to be used for Basic Auth
    "password",                 // String, password to be used for Basic Auth
    "https://url.to/endpoint"   // String, endpoint URL for Juridisk Logg
);
```

Kotlin:
```kotlin
val archiver = RestArchiver(
    username = "username",          // String, username to be used for Basic Auth
    password = "password",          // String, password to be used for Basic Auth
    url = "https://url.to/endpoint" // String, endpoint URL for Juridisk Logg
)
```

#### Archive a document

Java:
```java
String archiveId = archiver.archiveDocument(request);
```

Kotlin:
```kotlin
val archiveId = archiver.archiveDocument(request)
```

## Contact
Pull requests are welcome, otherwise create an issue here.
