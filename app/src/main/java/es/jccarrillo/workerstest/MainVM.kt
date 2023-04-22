package es.jccarrillo.workerstest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jccarrillo.workerstest.logger.LoggerService
import es.jccarrillo.workerstest.workers.WorkerController
import es.jccarrillo.workerstest.workers.WorkerScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val scheduler: WorkerScheduler,
    private val logger: LoggerService
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val list = mutableListOf<String>()
            logger.read().toList(list)

            list.reverse()

            _state.update {
                it.copy(
                    log = list,
                    singleWorkerOn = scheduler.single.isScheduled(),
                    periodicWorkerOn = scheduler.periodic.isScheduled()
                )
            }
        }
    }

    private fun scheduleWorker(controller: WorkerController, on: Boolean) {
        if (on) {
            controller.schedule()
        } else {
            controller.unSchedule()
        }

    }

    fun changeSingleWorkerStatus(on: Boolean) {
        viewModelScope.launch {
            _state.update {
                scheduleWorker(scheduler.single, on)
                it.copy(singleWorkerOn = scheduler.single.isScheduled())
            }
        }
    }

    fun changePeriodicWorkerStatus(on: Boolean) {
        viewModelScope.launch {
            _state.update {
                scheduleWorker(scheduler.periodic, on)
                it.copy(periodicWorkerOn = scheduler.periodic.isScheduled())
            }
        }
    }

    fun onRefresh() = refresh()
}