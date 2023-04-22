package es.jccarrillo.workerstest.workers.single

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import es.jccarrillo.workerstest.logger.LoggerService

@HiltWorker
class SingleWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val loggerService: LoggerService
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        loggerService.write("SingleWorker", "I've been launched")
        return Result.success()
    }
}