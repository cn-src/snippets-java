subprojects {
    apply(plugin = "maven-publish")

    group = "com.github.cn-src.snippets-java"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
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

