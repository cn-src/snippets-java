plugins {
    id("snippets-starter")
}
val springdocVersion: String by project
dependencies {
    api(project(":snippets-springdoc"))
    api("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")
}

