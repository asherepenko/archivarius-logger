package com.sherepenko.android.archivarius.logger

import androidx.annotation.VisibleForTesting
import com.sherepenko.android.archivarius.Archivarius
import com.sherepenko.android.logger.BaseLogger
import com.sherepenko.android.logger.BaseLoggerParams
import com.sherepenko.android.logger.LogContext

open class ArchivariusLogger
@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
@JvmOverloads
protected constructor(
    logWriter: ArchivariusLogWriter,
    logContext: LogContext = emptyMap()
) : BaseLogger(logWriter, logContext) {

    public override val logWriter: ArchivariusLogWriter
        get() = super.logWriter as ArchivariusLogWriter

    constructor(applicationId: String, archivarius: Archivarius) :
        this(
            ArchivariusLogWriter(
                archivarius
            ),
            mapOf(BaseLoggerParams.APPLICATION_ID to applicationId)
        )

    constructor(applicationId: String, logWriter: ArchivariusLogWriter) :
        this(logWriter, mapOf(BaseLoggerParams.APPLICATION_ID to applicationId))
}
