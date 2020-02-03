package com.github.korosuke613.vdmppLanguageServer

import com.fujitsu.vdmj.ast.definitions.ASTDefinitionList
import com.fujitsu.vdmj.lex.Dialect
import com.fujitsu.vdmj.lex.LexTokenReader
import com.fujitsu.vdmj.syntax.DefinitionReader
import org.eclipse.lsp4j.*

class Vdmpp(val textDocumentItem: TextDocumentItem){
    lateinit var definitionReader: DefinitionReader
    private lateinit var astDefinitions: ASTDefinitionList
    lateinit var publishDiagnosticsParams: PublishDiagnosticsParams

    init {
        updateVdmppFile()
    }

    fun updateVdmppFile(){
        val lexer = LexTokenReader(textDocumentItem.text, Dialect.VDM_PP)
        definitionReader = DefinitionReader(lexer)
        astDefinitions = definitionReader.readDefinitions()

        val diagnostics = ArrayList<Diagnostic>()
        definitionReader.errors.forEach { e ->
            val range = Range(
                    Position(e.location.startLine - 1, e.location.startPos - 1),
                    Position(e.location.endLine - 1, e.location.endPos - 1)
            )
            diagnostics.add(Diagnostic(range, e.message))
        }
        publishDiagnosticsParams = PublishDiagnosticsParams(textDocumentItem.uri, diagnostics)
    }
}