plugins {
    id("snippets-conventions")
    id("io.ebean") version "12.11.2"
}
val jooqVersion: String by project
val springdocVersion: String by project
dependencies {
    api("io.ebean:ebean")
    annotationProcessor("io.ebean:querybean-generator")
    api(project(":snippets-jooq"))
    api(project(":snippets-spring-exception"))

    compileOnly("com.fasterxml.jackson.core:jackson-annotations")

    compileOnly("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")
    optionalApi("org.springframework.boot:spring-boot-starter-web")
    optionalApi("org.springframework.boot:spring-boot-starter-security")
    optionalApi("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    testImplementation("io.ebean:ebean-test")
    testImplementation("com.h2database:h2")
}