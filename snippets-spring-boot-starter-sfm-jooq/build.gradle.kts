plugins {
    id("snippets-starter")
}
val simpleflatmapperVersion: String by project
dependencies {
    api("org.simpleflatmapper:sfm-jooq:${simpleflatmapperVersion}")
}

