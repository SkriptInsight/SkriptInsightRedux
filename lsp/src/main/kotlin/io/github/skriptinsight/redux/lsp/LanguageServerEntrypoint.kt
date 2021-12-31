package io.github.skriptinsight.redux.lsp

import org.eclipse.lsp4j.launch.LSPLauncher.Builder
import org.eclipse.lsp4j.services.LanguageClient
import java.io.InputStream
import java.util.concurrent.Executors
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        handleFatalException(e)
    }
    try {
        val server = SkriptInsightReduxLanguageServer()
        val input = ExitOnClose(System.`in`)
        val threads = Executors.newSingleThreadExecutor { Thread(it, "LSP-Client") }
        val launcher = Builder<LanguageClient>()
            .setLocalService(server)
            .setRemoteInterface(LanguageClient::class.java)
            .setInput(input)
            .setOutput(System.out)
            .setExecutorService(threads)
            .setExceptionHandler {
                handleFatalException(it)
            }
            .wrapMessages { it }
            .create()

        server.connect(launcher.remoteProxy)
        launcher.startListening().get()
    } catch (e: Exception) {
        handleFatalException(e)
    }
}

private fun handleFatalException(e: Throwable): Nothing {
    System.err.println("Exception thrown inside Language server: ")
    e.printStackTrace(System.err)
    exitProcess(1)
}

private class ExitOnClose(private val delegate: InputStream) : InputStream() {
    override fun read(): Int = exitIfNegative { delegate.read() }

    override fun read(b: ByteArray): Int = exitIfNegative { delegate.read(b) }

    override fun read(b: ByteArray, off: Int, len: Int): Int = exitIfNegative { delegate.read(b, off, len) }

    private fun exitIfNegative(call: () -> Int): Int {
        val result = call()

        if (result < 0) {
            System.err.println("System.in has closed, exiting")
            exitProcess(0)
        }

        return result
    }
}