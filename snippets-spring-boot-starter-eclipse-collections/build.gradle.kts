plugins {
    id("snippets-starter")
}
val eclipseCollectionsVersion: String by project
val jacksonDatatypesCollectionsVersion: String by project
dependencies {
    api("org.eclipse.collections:eclipse-collections:$eclipseCollectionsVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-eclipse-collections:$jacksonDatatypesCollectionsVersion")
}