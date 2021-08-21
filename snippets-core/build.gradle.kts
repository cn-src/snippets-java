plugins {
    id("snippets-conventions")
}
val commonsLang3Version: String by project
val commonsCollections4Version: String by project
val springdocVersion: String by project
dependencies {
    api("org.apache.commons:commons-lang3:$commonsLang3Version")
    api("org.apache.commons:commons-collections4:$commonsCollections4Version")

    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")

    testImplementation(project(":snippets-jackson"))
    testImplementation("com.h2database:h2")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}