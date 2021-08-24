plugins {
    id("snippets-conventions")
}
val hutoolVersion: String by project
val springdocVersion: String by project
dependencies {
    api("cn.hutool:hutool-core:$hutoolVersion")

    optionalApi("org.springframework.data:spring-data-commons")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")

    testImplementation(project(":snippets-jackson"))
    testImplementation("com.h2database:h2")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}