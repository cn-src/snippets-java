subprojects {
    apply(plugin = "maven-publish")

    group = "com.github.cn-src.snippets-java"
    version = "0.0.1-SNAPSHOT"

    repositories {
        repositories {
            maven { url = uri("https://maven.aliyun.com/repository/public/") }
            mavenLocal()
            mavenCentral()
        }
    }
    configure<PublishingExtension> {
        repositories {
            maven {
                val releasesRepoUrl = uri("$buildDir/repos/releases")
                val snapshotsRepoUrl = uri("$buildDir/repos/snapshots")
                val isSnapshot = (version as String).endsWith("SNAPSHOT")
                url = if (isSnapshot) snapshotsRepoUrl else releasesRepoUrl
            }
        }
    }
}