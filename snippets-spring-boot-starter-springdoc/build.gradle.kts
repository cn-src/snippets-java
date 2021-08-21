plugins {
    id("snippets-conventions")
}
val springdocVersion: String by project
dependencies {
    api(project(":snippets-spring-boot-autoconfigure"))
    api(project(":snippets-spring-boot-starter"))
    api("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")

    optionalApi("org.springframework.boot:spring-boot-starter-data-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}