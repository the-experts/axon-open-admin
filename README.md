# Axon Open Admin

We at [codecentric Netherlands](https://codecentric.nl) love Axon Framework and how easy it makes Event-Sourcing and CQRS. However, it's not always possible or desirable to run Axon Server so you can easily manage events and tokens. 

This is where Axon Open Admin comes in. It hooks into the JPA or Jdbc Tokenstores and lets you the containing tokens, such as splitting, merging, and the likes. Clone the project and fire up the demo to see it in real life, or just add the Starter to your Spring Boot application!
![](resources/teaser.png)


Note that this is a project currently in its initial development. We will support the project as well as we can, create a Github issue if anything is amiss. 

## How to install

Add the following dependency to your maven project:

```
<dependency>
    <groupId>nl.codecentric.axon-open-admin</groupId>
    <artifactId>axon-open-admin</artifactId>
    <version>0.0.8</version>
</dependency>
```

When your application now boots you can access the administration interface at `/(your-context-path)/axon-admin/`. Enjoy!

We recommend configuring Spring Boot Security to secure these endpoints, as they are open to abuse. 

## Considerations
There are a few things to keep in mind if you are planning on using this library:

- It only supports Axon 4.5. We are currently not planning on supporting older versions, since the working of this library depends on the private API of Axon Framework at the moment.
- There is a known issue with `spring-boot-devtools`. With devtools enabled your application will fail to boot.

