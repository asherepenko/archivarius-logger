import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    id("com.sherepenko.gradle.plugin-build-version") version "0.2.3"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.0"
    id("org.jetbrains.dokka") version "1.4.10"
    kotlin("android")
    kotlin("android.extensions")
}

val archivesBaseName = "archivarius-logger"

group = "com.github.asherepenko"
version = buildVersion.versionName

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(30)
        versionCode = buildVersion.versionCode
        versionName = buildVersion.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", archivesBaseName)
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        ignore("InvalidPackage")
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        getByName("release") {
            isZipAlignEnabled = true
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
    testImplementation("junit:junit:4.13")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation("androidx.test:runner:1.3.0")
    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.2")
    testImplementation("org.robolectric:robolectric:4.4")
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
