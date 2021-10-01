import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    id("com.sherepenko.gradle.plugin-build-version") version "0.2.3"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("org.jetbrains.dokka") version "1.4.30"
    id("org.jetbrains.kotlin.plugin.parcelize")
    kotlin("android")
}

val archivesBaseName = "archivarius-logger"

group = "com.github.asherepenko"
version = buildVersion.versionName

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 19
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", archivesBaseName)
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    lint {
        isCheckDependencies = true
        ignore("InvalidPackage")
        disable("InvalidPeriodicWorkRequestInterval")
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

ktlint {
    verbose.set(true)
    android.set(true)

    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
}

val archivariusVersion = "1.0.5"
val loggerVersion = "1.0.3"

dependencies {
    api("com.github.asherepenko:android-archivarius:$archivariusVersion")
    api("com.github.asherepenko:android-logger:$loggerVersion")
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("org.robolectric:robolectric:4.6.1")
}

tasks {
    val javadocJar by registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(dokkaHtml)
    }

    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

    artifacts {
        archives(javadocJar)
        archives(sourcesJar)
    }
}
