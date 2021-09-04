plugins {
    id("snippets-conventions")
}

val ebeanVersion: String by project
dependencies {
    api("io.ebean:ebean:$ebeanVersion")
}