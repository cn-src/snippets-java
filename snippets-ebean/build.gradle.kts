plugins {
    id("snippets-conventions")
    id("io.ebean") version "12.12.3"
}

val ebeanVersion: String by project
dependencies {
    api(project(":snippets-core"))
    api("io.ebean:ebean:$ebeanVersion")

    testImplementation("io.ebean:ebean-test")
    testImplementation("com.h2database:h2")
    testImplementation(project(":snippets-test"))
}