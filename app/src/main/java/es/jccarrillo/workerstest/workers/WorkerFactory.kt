package es.jccarrillo.workerstest.workers

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.Worker
import es.jccarrillo.workerstest.workers.periodic.PeriodicWorker
import es.jccarrillo.workerstest.workers.single.SingleWorker
import java.util.concurrent.TimeUnit

object WorkerFactory {

    const val periodicTag = "CustomFactory:PeriodicWorker"
    const val singleTag = "CustomFactory:SingleWorker"

    fun single(): WorkRequest {
        val delay = 20L

        return OneTimeWorkRequestBuilder<SingleWorker>()
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .addTag(singleTag)
            .build()
    }

    fun periodic(): WorkRequest {
        val each = 20L
        return PeriodicWorkRequestBuilder<PeriodicWorker>(each, TimeUnit.MINUTES)
            .addTag(periodicTag)
            .build()

    }
}