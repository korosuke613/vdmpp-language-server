package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.launch.LSPLauncher
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket
import org.slf4j.bridge.SLF4JBridgeHandler


object App {
    private val logger = LoggerFactory.getLogger(App.javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        SLF4JBridgeHandler.removeHandlersForRootLogger()
        SLF4JBridgeHandler.install()

        logger.info( "Starting server" )
        try {
            val server = VdmppLanguageServer()
            if(args.isEmpty()){
                throw IllegalArgumentException("You should add argument. ('-stdio' or PORT)")
            }
            val launcher = if(args.asList().contains("-stdio")) {
                LSPLauncher.createServerLauncher(server, System.`in`, System.out)
            }else{
                val port = args[0]
                val socket = Socket("localhost", port.toInt())
                LSPLauncher.createServerLauncher(server, socket.getInputStream(), socket.getOutputStream())
            }
            val client = launcher.remoteProxy
            server.connect(client)
            launcher.startListening()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}