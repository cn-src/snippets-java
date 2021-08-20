plugins {
    id("snippets-conventions")
}
val commonsLang3Version: String by project
dependencies {
    api("org.apache.commons:commons-lang3:$commonsLang3Version")
    api("org.springframework:spring-beans")
    implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    implementation("org.glassfish:jakarta.el:4.0.1")
}