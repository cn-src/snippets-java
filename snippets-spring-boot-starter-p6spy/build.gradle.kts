plugins {
    id("snippets-starter")
}
val datasourceDecoratorVersion: String by project
dependencies {
    api(project(":snippets-p6spy"))
    api("com.github.gavlyukovskiy:p6spy-spring-boot-starter:$datasourceDecoratorVersion")
}

