plugins {
    id("snippets-conventions")
}
val jooqVersion: String by project
val handlebarsVersion: String by project
val classgraphVersion: String by project
dependencies {
    api(project(":snippets-jooq"))
    api("org.jooq:jooq-codegen:$jooqVersion")
    api("com.github.jknack:handlebars:$handlebarsVersion")
    api("io.github.classgraph:classgraph:$classgraphVersion")

    testImplementation("com.h2database:h2")
    testImplementation("org.postgresql:postgresql")
    testImplementation("org.springframework.data:spring-data-jdbc")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}