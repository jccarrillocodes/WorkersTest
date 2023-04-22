package es.jccarrillo.workerstest.logger

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class LocalLogger @Inject constructor(@ApplicationContext private val context: Context) {

    private val file: File
        get() = File(context.filesDir, "logger.txt")

    fun write(str: String) {
        FileOutputStream(file, true).use { fos ->
            OutputStreamWriter(fos, StandardCharsets.UTF_8).use { it ->
                BufferedWriter(it).use { writer ->
                    writer.write(str)
                }
            }
        }
    }

    fun read() = flow {
        runCatching {
            FileInputStream(file).use { fis ->
                InputStreamReader(fis, StandardCharsets.UTF_8).use { isr ->
                    BufferedReader(isr).use {
                        try {
                            while (it.ready()) {
                                emit(it.readLine())
                            }
                        } catch (e: Exception) {
                            // EOF
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
        .flowOn(Dispatchers.IO)
}