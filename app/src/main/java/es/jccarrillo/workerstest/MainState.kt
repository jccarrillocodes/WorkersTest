package es.jccarrillo.workerstest

data class MainState(
    val periodicWorkerOn: Boolean = false,
    val singleWorkerOn: Boolean = false,
    val log: List<String> = emptyList()
)