plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.kotlin.kapt") version "1.6.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.3.2"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.10"
}

version = "0.1"
group = "com.login"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    //Security
    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.security:micronaut-security-oauth2")
    implementation("io.micronaut.security:micronaut-security-annotations")
    //Okhttp
    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("com.nimbusds:nimbus-jose-jwt:9.14")
    implementation("org.jsoup:jsoup:1.13.1")
    //Mongo
    implementation("io.micronaut.mongodb:micronaut-mongo-sync")
    //NoArg
    implementation("org.jetbrains.kotlin:kotlin-noarg")
    //Redis
    implementation("io.micronaut.redis:micronaut-redis-lettuce")

    noArg {
        annotation("com.login.commons.annotations.NoArg")
    }
}


application {
    mainClass.set("com.login.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.login.*")
    }
}


