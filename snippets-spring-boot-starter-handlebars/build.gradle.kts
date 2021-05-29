plugins {
    id("snippets-starter")
}
val handlebarsVersion: String by project
dependencies {
    api("com.github.jknack:handlebars:$handlebarsVersion")
}