plugins {
    `java-library`
}
dependencies {
    api(project(":snippets-spring-boot-autoconfigure"))
    api(project(":snippets-spring-exception"))
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}