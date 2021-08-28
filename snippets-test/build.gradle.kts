plugins {
    id("snippets-conventions")
}

val hutoolVersion: String by project

dependencies {
    api("cn.hutool:hutool-core:$hutoolVersion")
    api(project(":snippets-jackson"))

    optionalApi("org.skyscreamer:jsonassert")
    optionalApi("com.h2database:h2")
}