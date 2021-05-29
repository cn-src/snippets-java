plugins {
    id 'snippets-starter'
}

dependencies {
    api project(':snippets-springdoc')
    api "org.springdoc:springdoc-openapi-webmvc-core:${springdocVersion}"
}

