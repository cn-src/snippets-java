plugins {
    java
    id("de.benediktritter.maven-plugin-development") version "0.3.1"
}
java.sourceCompatibility = JavaVersion.VERSION_1_8
val deps by configurations.creating
mavenPlugin {
    dependencies.set(deps)
}
val classgraphVersion: String by project
val handlebarsVersion: String by project
dependencies {
    implementation(project(":snippets-jooq-codegen"))
    implementation("org.apache.maven:maven-plugin-api:3.8.1")
    implementation("org.apache.maven:maven-core:3.8.1")
    implementation("io.github.classgraph:classgraph:$classgraphVersion")
    implementation("com.github.jknack:handlebars:$handlebarsVersion")
    compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.6.0")
    deps(project(":snippets-jooq-codegen"))
    deps("io.github.classgraph:classgraph")
    deps("com.github.jknack:handlebars")
}
java {
    withSourcesJar()
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}