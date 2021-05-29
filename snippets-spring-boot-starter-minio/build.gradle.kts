plugins {
    id("snippets-starter")
}
val minioVersion: String by project
dependencies {
    api("io.minio:minio:$minioVersion")
}

