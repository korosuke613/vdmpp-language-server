package com.github.korosuke613.vdmppLanguageServer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class VdmppLanguageServerTest {
    private val vdmppLanguageServer = VdmppLanguageServer()

    @Test
    fun initialized() {
        assert(true)
        vdmppLanguageServer.initialized()
    }
}