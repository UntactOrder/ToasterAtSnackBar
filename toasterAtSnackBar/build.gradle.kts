import org.jetbrains.compose.compose
import org.jetbrains.dokka.gradle.DokkaTask

group = project.properties["lib.organization"]!!
version = rootProject.properties["lib.version"]!!

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("signing")
}

val dokkaHtml by tasks.getting(DokkaTask::class)

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        artifact(javadocJar)
        groupId = rootProject.extra["lib.organization"] as String
        version = rootProject.extra["lib.version"] as String
        artifactId = "toasterAtSnackBar"

        pom {
            name.set("ToasterAtSnackBar")
            description.set("Toast and SnackBar Lib contains design presets for Kotlin Multi-platform written with Compose.")
            url.set("https://github.com/UntactOrder/ToasterAtSnackBar/tree/main")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("UntactOrder")
                    name.set("UntactOrder Developers")
                    email.set("untactorder@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/UntactOrder/ToasterAtSnackBar.git")
                developerConnection.set("scm:git:ssh://github.com/UntactOrder/ToasterAtSnackBar.git")
                url.set("https://github.com/UntactOrder/ToasterAtSnackBar/tree/main")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.extra["signing_key_id"] as String,
        rootProject.extra["signing_secret_key"] as String,
        rootProject.extra["signing_password"] as String
    )
    sign(publishing.publications)
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "toasterAtSnackBar"
        }
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.ui)
                api(compose.runtime)
                api(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) api(compose.material3)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.compose.ui:ui-tooling-preview:1.3.0-rc01")
                api("androidx.compose.material3:material3:1.0.0-rc01")
                api("androidx.activity:activity-compose:1.6.0")
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("androidx.test.ext:junit-ktx:1.1.3")
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdk = (rootProject.extra["android.sdk.target.version"] as String).toInt()
    buildToolsVersion = rootProject.extra["android.build_tool.version"] as String
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (rootProject.extra["android.sdk.min.version"] as String).toInt()
        targetSdk = (rootProject.extra["android.sdk.target.version"] as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose.ext.version"] as String
    }
    packagingOptions {
        resources {
            merges += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/LICENSE*"
            )
            pickFirsts += "/bundle.properties"
        }
    }
}
