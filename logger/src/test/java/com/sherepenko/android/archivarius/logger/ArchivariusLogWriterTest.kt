package com.sherepenko.android.archivarius.logger

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import com.sherepenko.android.archivarius.Archivarius
import com.sherepenko.android.archivarius.ArchivariusAnalytics
import com.sherepenko.android.archivarius.ArchivariusStrategy
import com.sherepenko.android.archivarius.data.LogType
import com.sherepenko.android.archivarius.uploaders.LogUploadWorker
import com.sherepenko.android.archivarius.uploaders.LogUploader
import com.sherepenko.android.logger.BaseLoggerParams
import com.sherepenko.android.logger.LogLevel
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArchivariusLogWriterTest {

    private lateinit var archivarius: Archivarius

    private lateinit var logWriter: ArchivariusLogWriter

    @Before
    fun setUp() {
        ArchivariusAnalytics.init(TestArchivariusAnalytics())
        ArchivariusStrategy.init(TestArchivariusStrategy(getApplicationContext()))
        archivarius = mockk(relaxed = true)
        logWriter = ArchivariusLogWriter(archivarius)
    }

    @Test
    fun shouldWriteToArchivarius() {
        val logContext = mapOf(
            BaseLoggerParams.APPLICATION_ID to BuildConfig.LIBRARY_PACKAGE_NAME,
            BaseLoggerParams.TAG to "test"
        )

        logWriter.write(LogLevel.INFO, logContext)

        verify { archivarius.log(any()) }
    }
}

private class TestArchivariusAnalytics : ArchivariusAnalytics.ArchivariusAnalyticsImpl {

    override fun reportToCrashlytics(tag: String, e: Throwable) {
        throw AssertionError(e)
    }
}

private class TestArchivariusStrategy(
    context: Context
) : ArchivariusStrategy.ArchivariusStrategyImpl {

    override val isInDebugMode: Boolean = true

    override val isLogcatEnabled: Boolean = true

    override val authority: String = ""

    override val logName: String = Archivarius.DEFAULT_LOG_NAME

    override val rotateFilePostfix: String = ""

    override val parentLogDir: File = context.filesDir

    override val logUploader: LogUploader =
        object : LogUploader {
            override fun uploadLog(logFile: File, logType: LogType) =
                Unit
        }

    override val logUploadWorker: Class<out ListenableWorker> =
        LogUploadWorker::class.java
}
