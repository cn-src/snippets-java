plugins {
    id("snippets-conventions")
}

dependencies {
    api(project(":snippets-core"))
    optionalApi("org.springframework:spring-web")
    optionalApi("org.springframework:spring-context")
}