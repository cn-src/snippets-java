plugins {
    id("snippets-conventions")
}
val simpleflatmapperVersion: String by project
val easyBatchVersion: String by project
dependencies {
    api(project(":snippets-jooq"))
    api("org.simpleflatmapper:sfm-jdbc:$simpleflatmapperVersion")
    api("com.github.cn-src.easy-batch:easy-batch-jdbc:$easyBatchVersion")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.data:spring-data-jdbc")
}