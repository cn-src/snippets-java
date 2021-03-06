//import cn.javaer.snippets.jooq.codegen.withentity.CodeGenConfig
//import cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool
//
//import java.nio.file.Paths

//buildscript {
//    dependencies {
//        classpath(project(":snippets-jooq-codegen"))
//    }
//}
plugins {
    id("snippets-conventions")
}
val jooqVersion: String by project
val springdocVersion: String by project
dependencies {
    api(project(":snippets-jooq"))
    api(project(":snippets-spring-exception"))
    api("org.jooq:jooq:$jooqVersion")

    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("org.springframework.data:spring-data-jdbc")

    compileOnly("org.springdoc:springdoc-openapi-webmvc-core:$springdocVersion")
    optionalApi("org.springframework.boot:spring-boot-starter-web")
    optionalApi("org.springframework.boot:spring-boot-starter-security")
    optionalApi("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    testImplementation("org.postgresql:postgresql")
    testImplementation("com.playtika.testcontainers:embedded-postgresql")
}

//task("jooq-generate") {
//    dependsOn compileJava
//    doLast {
//        def dir = Paths.get(System.getProperty("user.dir"), "snippets-security/src/main/java").toString()
//        def classLoader = new URLClassLoader(sourceSets.main.runtimeClasspath.files.collect { it.toURI().toURL() } as URL[])
//        def config = CodeGenConfig.builder().classLoader(classLoader)
//                .generatedSrcRootDir(dir)
//                .generatedSrcPackage("cn.javaer.snippets.security.rbac.gen")
//                .scanClassName("cn.javaer.snippets.security.rbac.Permission")
//                .scanClassName("cn.javaer.snippets.security.rbac.PermissionDetails")
//                .scanClassName("cn.javaer.snippets.security.rbac.Role")
//                .scanClassName("cn.javaer.snippets.security.rbac.RoleDetails")
//                .scanClassName("cn.javaer.snippets.security.rbac.RolePermission")
//                .scanClassName("cn.javaer.snippets.security.rbac.UserPermission")
//                .build()
//        CodeGenTool.generate(config)
//    }
//}