# Logger with Archivarius

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![JitCI](https://jitci.com/gh/asherepenko/archivarius-logger/svg)](https://jitci.com/gh/asherepenko/archivarius-logger)
[![Latest Version](https://jitpack.io/v/asherepenko/archivarius-logger.svg)](https://jitpack.io/#asherepenko/archivarius-logger) 

Base JSON Logger implementation for Android. Based on [Logger](https://github.com/asherepenko/android-logger) and [Archivarius](https://github.com/asherepenko/android-archivarius).

- [Archivarius README.md](https://github.com/asherepenko/android-archivarius/blob/master/README.md)
- [Logger README.md](https://github.com/asherepenko/android-logger/blob/master/README.md)

## How to

**Step 1.** Add the JitPack repository to your build file

Add it in your root `build.gradle.kts` at the end of repositories:

```kotlin
allprojects {
    repositories {
        maven(url = "https://jitpack.io")
    }
}
```

**Step 2.** Add the dependency

```kotlin
dependencies {
    implementation("com.github.asherepenko:archivarius-logger:x.y.z")
}
```

## Initial setup

Archivarius has to be initialized with two strategies before any interaction:

```kotlin
ArchivariusStrategy.init(object : ArchivariusStrategyImpl {
    override val isInDebugMode: Boolean = true

    override val isLogcatEnabled: Boolean = true

    override val authority: String = ""

    override val rotateFilePostfix: String = ""

    override val logName: String = "log"

    override val parentLogDir: File = File("/")

    override val logUploader: LogUploader = LogUploader()

    override val logUploadWorker: Class<out ListenableWorker> = ListenableWorker::class.java
})

ArchivariusAnalytics.init(object : ArchivariusAnalyticsImpl {
    override fun reportToCrashlytics(tag: String, e: Throwable) {
    }
})
```

## Format

Required logger fields list:
- `message`
- `timestamp` ([RFC 3339](https://tools.ietf.org/html/rfc3339), with fractional seconds, nanoseconds when possible)
- `log_level` (one of `debug`, `info`, `warning`, `error`)
- `application_id`

## Contextual Data

Depending on the context, log records may include the following fields:
- `tag`
- `user_id`
- `app_install_id`
- `device_serial`
- `device_id`
- `job_id` (for background tasks)
- `exception` (this field might contain error stacktrace)

## Usage examples

```kotlin
// Create logger instance
val logger = ArchivariusLogger(
    BuildConfig.APPLICATION_ID,
    Archivarius.Builder(context).build()
)

// Write logs with Logger
logger.info("This is a test info message")
```

- [Sample App](https://github.com/asherepenko/s3-json-logger)
