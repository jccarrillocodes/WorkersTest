package es.jccarrillo.workerstest.logger

import java.util.Date
import javax.inject.Inject

class LoggerService @Inject constructor(private val localLogger: LocalLogger) {
    fun write(who: String, str: String) {
        val now = Date().toString()
        localLogger.write("$now [$who] $str\n")
    }

    fun read() = localLogger.read()
}