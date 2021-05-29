plugins {
    id("snippets-conventions")
}
val kryoVersion: String by project
dependencies {
    api("com.esotericsoftware:kryo:$kryoVersion")

    optionalApi("org.jooq:jooq")
    optionalApi("io.github.classgraph:classgraph")
    optionalApi("org.eclipse.collections:eclipse-collections")
}