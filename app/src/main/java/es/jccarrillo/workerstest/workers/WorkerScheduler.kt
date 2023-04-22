package es.jccarrillo.workerstest.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class WorkerScheduler @Inject constructor(@ApplicationContext context: Context) {
    val single: WorkerController = SingleWorkerController(context)
    val periodic: WorkerController = PeriodicWorkerController(context)
}

interface WorkerController {
    fun schedule()
    fun unSchedule()
    fun isScheduled(): Boolean
}

internal open class AutoWorkController(
    private val context: Context,
    private val tag: String,
    private val factory: () -> WorkRequest
) : WorkerController {
    override fun schedule() {
        val request = factory()
        val workerInstance = WorkManager.getInstance(context)

        when (request) {
            is OneTimeWorkRequest -> workerInstance
                .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, request)

            is PeriodicWorkRequest -> workerInstance
                .enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.UPDATE, request)

            else -> workerInstance.enqueue(request)
        }


    }

    override fun unSchedule() {
        val workerInstance = WorkManager
            .getInstance(context)

        workerInstance.cancelAllWorkByTag(tag)
        workerInstance.cancelUniqueWork(tag)

    }

    override fun isScheduled(): Boolean {
        return runCatching {
            WorkManager
                .getInstance(context)
                .getWorkInfosByTag(tag)
                .takeIf {
                    !it.isDone && !it.isCancelled
                }?.get()?.first {
                    it.state == WorkInfo.State.ENQUEUED
                } != null
        }.getOrElse { false }
    }
}

private class SingleWorkerController(context: Context) :
    AutoWorkController(context, WorkerFactory.singleTag, { WorkerFactory.single() })

private class PeriodicWorkerController(context: Context) :
    AutoWorkController(context, WorkerFactory.periodicTag, { WorkerFactory.periodic() })