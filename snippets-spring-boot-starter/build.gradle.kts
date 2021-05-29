plugins {
    `java-library`
}
dependencies {
    api(project(":snippets-spring-boot-autoconfigure"))
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
