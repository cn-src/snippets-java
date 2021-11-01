group = "com.github.cn-src.snippets-java"
version = "0.0.1-rc1"
subprojects {
    apply(plugin = "maven-publish")


    repositories {
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        mavenCentral()
    }
    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("https://maven.pkg.github.com/cn-src/snippets-java")
                credentials {
                    username =
                        project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
}