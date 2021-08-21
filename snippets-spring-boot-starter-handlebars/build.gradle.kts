plugins {
    id("snippets-conventions")
}
val handlebarsVersion: String by project
dependencies {
    optionalApi("org.springframework.boot:spring-boot-starter")
    api("com.github.jknack:handlebars:$handlebarsVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}