plugins {
    id("snippets-conventions")
}
val jooqVersion: String by project
dependencies {
    api(project(":snippets-core"))
    api(project(":snippets-spring"))
    api(project(":snippets-jackson"))
    api(project(":snippets-spring-exception"))

    optionalApi("org.jooq:jooq:$jooqVersion")
    optionalApi("org.springframework.boot:spring-boot-starter-web")

    testImplementation("com.h2database:h2")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}