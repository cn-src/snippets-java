plugins {
    id("snippets-conventions")
}
val commonsLang3Version: String by project
dependencies {
    api("org.apache.commons:commons-lang3:$commonsLang3Version")
    optionalApi("org.springframework:spring-beans")
    optionalApi("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    optionalApi("org.glassfish:jakarta.el:4.0.1")
}