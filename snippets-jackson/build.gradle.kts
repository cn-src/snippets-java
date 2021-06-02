plugins {
    id("snippets-conventions")
}
val jacksonVersion: String by project
dependencies {
    api(project(":snippets-core"))
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    optionalApi("org.jooq:jooq")

    testImplementation("com.h2database:h2")
}