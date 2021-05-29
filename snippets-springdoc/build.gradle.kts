plugins {
    id("snippets-conventions")
}
val springdocVersion: String by project
dependencies {
    api("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")
}