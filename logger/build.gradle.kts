import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    id("com.github.dcendents.android-maven") version "2.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("org.jetbrains.dokka") version "0.10.0"
    kotlin("android")
    kotlin("android.extensions")
}

val archivesBaseName = "archivarius-logger"
val buildVersion = BuildVersion(rootProject.file("version"))

group = "com.github.asherepenko"
version = buildVersion.versionName

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(29)
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
    testImplementation("androidx.test:core:1.2.0")
    testImplementation("androidx.test:runner:1.2.0")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("com.google.truth:truth:0.44")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.robolectric:robolectric:4.3.1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }

    val javadocJar by registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(dokka)
    }

    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

    val incrementMajor by registering(IncrementVersion::class) {
        increment = Increment.MAJOR
        version = buildVersion
    }

    val incrementMinor by registering(IncrementVersion::class) {
        increment = Increment.MINOR
        version = buildVersion
    }

    val incrementPatch by registering(IncrementVersion::class) {
        increment = Increment.PATCH
        version = buildVersion
    }

    artifacts {
        archives(javadocJar)
        archives(sourcesJar)
    }
}
