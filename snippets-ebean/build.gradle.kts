plugins {
    id("snippets-conventions")
}

val ebeanVersion: String by project
dependencies {
    api(project(":snippets-core"))
    api("io.ebean:ebean:$ebeanVersion")

    testImplementation("io.ebean:ebean-test")
    testImplementation("com.h2database:h2")
    testImplementation(project(":snippets-test"))
}