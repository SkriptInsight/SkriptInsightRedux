package io.github.skriptinsight.redux.lsp

import org.eclipse.lsp4j.launch.LSPLauncher
import java.io.InputStream
import java.util.concurrent.Executors
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val server = SkriptInsightReduxLanguageServer()
    val input = ExitOnClose(System.`in`)
    val threads = Executors.newSingleThreadExecutor { Thread(it, "client") }
    val launcher = LSPLauncher.createServerLauncher(server, input, System.out, threads) { it }
    server.connect(launcher.remoteProxy)
    launcher.startListening().get()
}


private class ExitOnClose(private val delegate: InputStream): InputStream() {
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