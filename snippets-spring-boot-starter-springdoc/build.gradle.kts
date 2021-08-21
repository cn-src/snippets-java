plugins {
    id("snippets-conventions")
}
val springdocVersion: String by project
dependencies {
    api(project(":snippets-springdoc"))
    api("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")

    optionalApi("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}