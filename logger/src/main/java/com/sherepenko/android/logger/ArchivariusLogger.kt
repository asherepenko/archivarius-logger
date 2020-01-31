package com.sherepenko.android.logger

import androidx.annotation.VisibleForTesting
import com.sherepenko.android.archivarius.Archivarius

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
            ArchivariusLogWriter(archivarius),
            mapOf(BaseLoggerParams.APPLICATION_ID to applicationId)
        )

    constructor(applicationId: String, logWriter: ArchivariusLogWriter) :
        this(logWriter, mapOf(BaseLoggerParams.APPLICATION_ID to applicationId))
}
