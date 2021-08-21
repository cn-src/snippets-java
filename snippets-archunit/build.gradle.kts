plugins {
    id("snippets-conventions")
}
val archunitVersion: String by project

dependencies {
    api("com.tngtech.archunit:archunit-junit5:$archunitVersion")
}