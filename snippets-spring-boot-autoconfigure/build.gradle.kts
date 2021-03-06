plugins {
    id("snippets-conventions")
}

dependencies {
    api(project(":snippets-core"))
    api(project(":snippets-spring"))
    optionalApi(project(":snippets-jooq"))
    optionalApi(project(":snippets-p6spy"))
    optionalApi(project(":snippets-springdoc"))
    optionalApi(project(":snippets-spring-exception"))

    optionalApi("org.springframework.boot:spring-boot-starter-web")
    optionalApi("org.springframework.boot:spring-boot-starter-data-jdbc")
    optionalApi("org.springframework.boot:spring-boot-starter-jooq")
    optionalApi("org.springframework.boot:spring-boot-starter-validation")
    optionalApi("org.springframework.boot:spring-boot-starter-security")
    optionalApi("org.springframework.boot:spring-boot-starter-test")
    optionalApi("com.github.gavlyukovskiy:p6spy-spring-boot-starter")
    optionalApi("org.springdoc:springdoc-openapi-data-rest")

    optionalApi("io.minio:minio")
    optionalApi("org.eclipse.collections:eclipse-collections")
    optionalApi("com.github.jknack:handlebars")
    optionalApi("org.simpleflatmapper:sfm-jooq")

    optionalApi("org.postgresql:postgresql")

    testImplementation("com.h2database:h2")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}