buildscript {
    val localProp = java.util.Properties()
    localProp.load(file("local.properties").inputStream())

    extra["kotlin_version"] = "1.7.10"
    extra["ktor_version"] = "2.0.3"
    extra["junit_version"] = "5.9.0"
    extra["compose_ext_version"] = "1.3.0-beta01"
    extra["moko_res_version"] = "0.20.1"
    extra["android_target_sdk_version"] = 33
    extra["android_min_sdk_version"] = 24
    extra["android_build_tool_version"] = "33.0.0"

    extra["organization"] = project.properties["lib.organization"]
    extra["version"] = project.properties["lib.version"]

    extra["ossrh_username"] = localProp.getProperty("ossrh.username") ?: ""
    extra["ossrh_password"] = localProp.getProperty("ossrh.password") ?: ""
    extra["sonatype_staging_profile_id"] = localProp.getProperty("sonatype.staging.profile_id") ?: ""
    extra["signing_key_id"] = localProp.getProperty("signing.key_id") ?: "your-key-id"
    extra["signing_secret_key"] = localProp.getProperty("signing.secret_key") ?: "your-secret-key"
    extra["signing_password"] = localProp.getProperty("signing.password") ?: "your-password"

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${project.extra["kotlin_version"]}")
        classpath("com.android.tools.build:gradle:7.3.0")
    }
}

group = project.properties["lib.organization"]!!
version = project.properties["lib.version"]!!

plugins {
    /**
     * Compose Gradle Plugin
     * https://plugins.gradle.org/plugin/org.jetbrains.compose
     */
    id("org.jetbrains.compose").version("1.2.0-alpha01-dev770").apply(true)
    /**
     * Sonatype Nexus Publish Plugin
     */
    id("io.github.gradle-nexus.publish-plugin").version("1.1.0").apply(true)
    /**
     * Dokka Gradle Plugin
     */
    id("org.jetbrains.dokka").version("1.7.10").apply(true)
}

nexusPublishing {
    repositories {
        create("myNexus") {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            stagingProfileId.set(project.extra["sonatype_staging_profile_id"] as String)
            username.set(project.extra["ossrh_username"] as String)
            password.set(project.extra["ossrh_password"] as String)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        google()

        maven {
            setUrl("https://jitpack.io")
            setUrl("https://kotlin.bintray.com/kotlinx")
            setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
