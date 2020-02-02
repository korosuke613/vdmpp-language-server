package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.launch.LSPLauncher
import java.io.IOException
import java.net.Socket


object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val port = args[0]
        try {
            val socket = Socket("localhost", port.toInt())
            val inputStream = socket.getInputStream()
            val outStream = socket.getOutputStream()
            val server = ExampleLanguageServer()
            val launcher = LSPLauncher.createServerLauncher(server, inputStream, outStream)
            val client = launcher.remoteProxy
            server.connect(client)
            launcher.startListening()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}