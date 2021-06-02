plugins {
    `java-platform`
}
val p = project
operator fun Project.get(prop: String) = project.property(prop)
dependencies {
    constraints {
        api(project(":snippets-core"))
        api(project(":snippets-easy-batch"))
        api(project(":snippets-jackson"))
        api(project(":snippets-jooq"))
        api(project(":snippets-jooq-codegen"))
        api(project(":snippets-jooq-codegen-maven-plugin"))
        api(project(":snippets-kryo"))
        api(project(":snippets-p6spy"))
        api(project(":snippets-spring"))
        api(project(":snippets-spring-boot-autoconfigure"))
        api(project(":snippets-spring-boot-starter"))
        api(project(":snippets-spring-boot-starter-data-jooq-jdbc"))
        api(project(":snippets-spring-boot-starter-eclipse-collections"))
        api(project(":snippets-spring-boot-starter-handlebars"))
        api(project(":snippets-spring-boot-starter-p6spy"))
        api(project(":snippets-spring-boot-starter-sfm-jooq"))
        api(project(":snippets-spring-boot-starter-springdoc"))
        api(project(":snippets-springdoc"))

        // 工具库
        api("org.jodd:jodd-util:${p["joddUtilVersion"]}")
        api("com.google.guava:guava:${p["guavaVersion"]}")
        api("org.eclipse.collections:eclipse-collections:${p["eclipseCollectionsVersion"]}")
        api("org.apache.commons:commons-collections4:${p["commonsCollections4Version"]}")
        api("commons-io:commons-io:${p["commonsIoVersion"]}")
        api("org.apache.commons:commons-lang3:${p["commonsLang3Version"]}")
        api("com.esotericsoftware:reflectasm:${p["reflectasmVersion"]}")

        api("cn.hutool:hutool-core:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-aop:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-bloomFilter:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-cache:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-crypto:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-db:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-dfa:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-pra:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-http:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-log:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-script:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-setting:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-system:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-cron:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-json:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-poi:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-captcha:${p["hutoolVersion"]}")
        api("cn.hutool:hutool-socket:${p["hutoolVersion"]}")

        // 注解库
        api("com.google.code.findbugs:jsr305:${p["jsr305Version"]}")
        api("org.jetbrains:annotations:${p["jetbrainsAnnotationsVersion"]}")

        // 缓存库
        api("org.ehcache:ehcache:${p["ehcacheVersion"]}")

        // 序列化
        api("com.fasterxml.jackson.datatype:jackson-datatype-eclipse-collections:${p["jacksonDatatypesCollectionsVersion"]}")
        api("com.esotericsoftware:kryo:${p["kryoVersion"]}")

        // 模板引擎
        api("com.github.jknack:handlebars:${p["handlebarsVersion"]}")
        api("com.deepoove:poi-tl:${p["poiTlVersion"]}")

        // 类信息扫描工具
        api("io.github.classgraph:classgraph:${p["classgraphVersion"]}")

        api("com.alibaba:easyexcel:${p["easyexcelVersion"]}")

        // SQL 日志统一输出工具
        api("p6spy:p6spy:${p["p6spyVersion"]}")

        // 集合查询引擎
        api("com.googlecode.cqengine:cqengine:${p["cqengineVersion"]}")

        // 对象存储服务-客户端
        api("io.minio:minio:${p["minioVersion"]}")

        // Java 架构检测框架
        api("com.tngtech.archunit:archunit-junit5:${p["archunitVersion"]}")

        api("org.mybatis:mybatis:${p["mybatisVersion"]}")
        api("org.mybatis:mybatis-spring:${p["mybatisSpringVersion"]}")
        api("org.mybatis.dynamic-sql:mybatis-dynamic-sql:${p["mybatisDynamicSqlVersion"]}")
        api("org.mybatis.caches:mybatis-ehcache:${p["mybatisEhcacheVersion"]}")
        api("org.mybatis.spring.boot:mybatis-spring-boot-starter:${p["mybatisSpringBootVersion"]}")
        api("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:${p["mybatisSpringBootVersion"]}")

        // web 反向代理
        api("com.github.mkopylec:charon-spring-webmvc:${p["charonVersion"]}")
        api("com.github.mkopylec:charon-spring-webflux:${p["charonVersion"]}")

        runtime("com.taobao.arthas:arthas-spring-boot-starter:${p["arthasSpringBootVersion"]}")
        api("com.baomidou:dynamic-datasource-spring-boot-starter:${p["dynamicDatasourceVersion"]}")
        api("com.github.ethancommitpush:spring-boot-starter-openfeign:${p["springBootOpenfeignVersion"]}")

        api("org.apache.shardingsphere:shardingsphere-jdbc-core:${p["shardingsphereVersion"]}")
        api("org.apache.shardingsphere.elasticjob:elasticjob-lite-core:${p["elasticjobVersion"]}")

        api("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${p["datasourceDecoratorVersion"]}")
        api("com.github.gavlyukovskiy:flexy-pool-spring-boot-starter:${p["datasourceDecoratorVersion"]}")

        api("org.springdoc:springdoc-openapi-ui:${p["springdocVersion"]}")
        api("org.springdoc:springdoc-openapi-webmvc-core:${p["springdocVersion"]}")
        api("org.springdoc:springdoc-openapi-security:${p["springdocVersion"]}")
        api("org.springdoc:springdoc-openapi-data-rest:${p["springdocVersion"]}")

        api("org.simpleflatmapper:sfm-csv:${p["simpleflatmapperVersion"]}")
        api("org.simpleflatmapper:sfm-poi:${p["simpleflatmapperVersion"]}")
        api("org.simpleflatmapper:sfm-jdbc:${p["simpleflatmapperVersion"]}")
        api("org.simpleflatmapper:sfm-jooq:${p["simpleflatmapperVersion"]}")
        api("org.simpleflatmapper:sfm-springjdbc:${p["simpleflatmapperVersion"]}")

        api("io.vavr:vavr:${p["vavrVersion"]}")
        api("io.vavr:vavr-jackson:${p["vavrVersion"]}")
        api("io.vavr:vavr-test:${p["vavrVersion"]}")
        api("io.vavr:vavr-match:${p["vavrVersion"]}")

        api("de.codecentric:spring-boot-admin-server:${p["springBootAdminVersion"]}")
        api("de.codecentric:spring-boot-admin-server-ui:${p["springBootAdminVersion"]}")
        api("de.codecentric:spring-boot-admin-client:${p["springBootAdminVersion"]}")
        api("de.codecentric:spring-boot-admin-starter-client:${p["springBootAdminVersion"]}")
        api("de.codecentric:spring-boot-admin-starter-server:${p["springBootAdminVersion"]}")
        api("de.codecentric:spring-boot-admin-server-cloud:${p["springBootAdminVersion"]}")

        api("com.github.cn-src.easy-batch:easy-batch-core:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-validation:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-flatfile:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-jdbc:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-jpa:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-jms:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-xml:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-json:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-pensions:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-spring:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-opencsv:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-apache-commons-csv:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-gson:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-xstream:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-jackson:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-hibernate:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-msexcel:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-univocity:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-yaml:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-integration:${p["easyBatchVersion"]}")
        api("com.github.cn-src.easy-batch:easy-batch-quartz:${p["easyBatchVersion"]}")

        api("org.jetbrains.kotlin:kotlin-stdlib:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-stdlib-js:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-stdlib-common:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-reflect:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-osgi-bundle:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-junit:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-junit5:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-testng:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-js:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-common:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-test-annotations-common:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-main-kts:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-script-runtime:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-script-util:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-scripting-common:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-scripting-jvm:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-scripting-jvm-host:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-scripting-ide-services:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-compiler:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-compiler-embeddable:${p["kotlinVersion"]}")
        api("org.jetbrains.kotlin:kotlin-daemon-client:${p["kotlinVersion"]}")

        api("org.codehaus.groovy:groovy:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-ant:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-astbuilder:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-bsf:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-cli-commons:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-cli-picocli:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-console:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-datetime:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-dateutil:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-docgenerator:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-groovydoc:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-groovysh:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-jaxb:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-jmx:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-json:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-jsr223:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-macro:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-nio:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-servlet:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-sql:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-swing:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-templates:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-test:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-test-junit5:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-testng:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-xml:${p["groovyVersion"]}")
        api("org.codehaus.groovy:groovy-yaml:${p["groovyVersion"]}")

        api("org.spockframework:spock-core:${p["spockVersion"]}")
        api("org.spockframework:spock-guice:${p["spockVersion"]}")
        api("org.spockframework:spock-junit4:${p["spockVersion"]}")
        api("org.spockframework:spock-spring:${p["spockVersion"]}")
        api("org.spockframework:spock-tapestry:${p["spockVersion"]}")
        api("org.spockframework:spock-unitils:${p["spockVersion"]}")

        api("org.testcontainers:postgresql:${p["testcontainersVersion"]}")
        api("org.testcontainers:mysql:${p["testcontainersVersion"]}")
        api("org.testcontainers:mariadb:${p["testcontainersVersion"]}")
        api("org.testcontainers:oracle-x:${p["testcontainersVersion"]}")
        api("org.testcontainers:elasticsearch:${p["testcontainersVersion"]}")
        api("org.testcontainers:spock:${p["testcontainersVersion"]}")
        api("org.testcontainers:junit-jupiter:${p["testcontainersVersion"]}")

        api("com.playtika.testcontainers:testcontainers-common:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-aerospike:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-couchbase:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-dynamodb:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-elasticsearch:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-google-pubsub:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-influxdb:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-kafka:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-keycloak:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-mariadb:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-memsql:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-mongodb:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-neo4j:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-oracle-xe:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-postgresql:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-rabbitmq:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-redis:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-vault:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-voltdb:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-mysql:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-minio:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-localstack:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-clickhouse:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-cassandra:${p["playtikaVersion"]}")
        api("com.playtika.testcontainers:embedded-selenium:${p["playtikaVersion"]}")

        api("io.zonky.test:embedded-database-spring-test-autoconfigure:${p["zonkySpringVersion"]}")
        api("io.zonky.test:embedded-database-spring-test:${p["zonkySpringVersion"]}")
        api("io.zonky.test:embedded-postgres:${p["zonkyVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-darwin-amd64:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-amd64:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-amd64-alpine:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-amd64-alpine-lite:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-windows-amd64:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-i386:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-i386-alpine:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-i386-alpine-lite:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm32v6:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm32v6-alpine:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm32v6-alpine-lite:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm32v7:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm64v8:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm64v8-alpine:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-arm64v8-alpine-lite:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-ppc64le:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-ppc64le-alpine:${p["zonkyPostgresVersion"]}")
        api("io.zonky.test.postgres:embedded-postgres-binaries-linux-ppc64le-alpine-lite:${p["zonkyPostgresVersion"]}")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["javaPlatform"])
        }
    }
}