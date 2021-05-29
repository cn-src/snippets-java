plugins {
    id("snippets-starter")
}
val springBootVersion: String by project
dependencies {
    api(project(":snippets-spring-boot-starter"))
    api("org.springframework.boot:spring-boot-starter-data-jdbc:$springBootVersion")
    api("org.springframework.boot:spring-boot-starter-jooq:$springBootVersion")
}
