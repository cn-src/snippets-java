plugins {
    id("snippets-conventions")
}
val hutoolVersion: String by project
val springdocVersion: String by project
dependencies {
    api("cn.hutool:hutool-core:$hutoolVersion")

    optionalApi("org.springframework.data:spring-data-commons")
    optionalImplementation("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations")

    testImplementation(project(":snippets-jackson"))
}