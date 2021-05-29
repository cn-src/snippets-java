plugins {
    id("snippets-conventions")
}
dependencies {
    api(project(":snippets-core"))
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    optionalApi("org.jooq:jooq")

    testImplementation("com.h2database:h2")
}