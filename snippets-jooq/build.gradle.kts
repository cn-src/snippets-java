plugins {
    id("snippets-conventions")
}
val jooqVersion: String by project
dependencies {
    api(project(":snippets-core"))
    api(project(":snippets-jackson"))
    api("org.jooq:jooq:$jooqVersion")

    optionalApi("org.springframework:spring-beans")
    optionalApi("org.postgresql:postgresql")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.data:spring-data-jdbc")
}