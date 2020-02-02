package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.services.*
import java.util.concurrent.CompletableFuture


class ExampleLanguageServer : LanguageServer, LanguageClientAware {
    private var client: LanguageClient? = null
    private var workspaceRoot: String? = null
    private var maxNumberOfProblems = 100

    override fun initialize(params: InitializeParams): CompletableFuture<InitializeResult?>? {
        workspaceRoot = params.rootPath
        val capabilities = ServerCapabilities()
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full)
        capabilities.setCodeActionProvider(false)
        capabilities.completionProvider = CompletionOptions(true, null)
        return CompletableFuture.completedFuture(InitializeResult(capabilities))
    }

    override fun shutdown(): CompletableFuture<Any?>? {
        return CompletableFuture.completedFuture(null)
    }

    override fun exit() {}

    private val fullTextDocumentService = object : FullTextDocumentService() {
        override fun completion(position: TextDocumentPositionParams?): CompletableFuture<CompletionList>? {
            val typescriptCompletionItem = CompletionItem()
            typescriptCompletionItem.label = "TypeScript"
            typescriptCompletionItem.kind = CompletionItemKind.Text
            typescriptCompletionItem.data = 1.0
            val javascriptCompletionItem = CompletionItem()
            javascriptCompletionItem.label = "JavaScript"
            javascriptCompletionItem.kind = CompletionItemKind.Text
            javascriptCompletionItem.data = 2.0
            val completions: MutableList<CompletionItem> = ArrayList()
            completions.add(typescriptCompletionItem)
            completions.add(javascriptCompletionItem)
            return CompletableFuture.completedFuture(CompletionList(false, completions))
        }

        override fun resolveCompletionItem(unresolved: CompletionItem): CompletableFuture<CompletionItem>? {
            if (unresolved.data == 1.0) {
                unresolved.detail = "TypeScript details"
                unresolved.setDocumentation("TypeScript documentation")
            } else if (unresolved.data == 2.0) {
                unresolved.detail = "JavaScript details"
                unresolved.setDocumentation("JavaScript documentation")
            }
            return CompletableFuture.completedFuture(unresolved)
        }

        override fun didChange(params: DidChangeTextDocumentParams) {
            super.didChange(params)
            val document: TextDocumentItem? = this.documents[params.textDocument.uri]
            if (document != null) {
                validateDocument(document)
            }
        }
    }

    override fun getTextDocumentService(): TextDocumentService? {
        return fullTextDocumentService
    }

    private fun validateDocument(document: TextDocumentItem) {
        val diagnostics: MutableList<Diagnostic> = ArrayList()
        val lines = document.text.split("\\r?\\n").toTypedArray()
        var problems = 0
        var i = 0
        while (i < lines.size && problems < maxNumberOfProblems) {
            val line = lines[i]
            val index = line.indexOf("typescript")
            if (index >= 0) {
                problems++
                val diagnostic = Diagnostic()
                diagnostic.severity = DiagnosticSeverity.Warning
                diagnostic.range = Range(Position(i, index), Position(i, index + 10))
                diagnostic.message = String.format(
                    "%s should be spelled TypeScript",
                    line.substring(index, index + 10)
                )
                diagnostic.source = "ex"
                diagnostics.add(diagnostic)
            }
            i++
        }
        client!!.publishDiagnostics(PublishDiagnosticsParams(document.uri, diagnostics))
    }

    override fun getWorkspaceService(): WorkspaceService {
        return object : WorkspaceService {
            override fun symbol(params: WorkspaceSymbolParams): CompletableFuture<List<SymbolInformation?>>? {
                return null
            }

            override fun didChangeConfiguration(params: DidChangeConfigurationParams) {
                val settings =
                    params.settings as Map<*, *>
                val languageServerExample =
                    settings["languageServerExample"] as Map<*, *>?
                maxNumberOfProblems =
                    ((languageServerExample!!["maxNumberOfProblems"] ?: 100.0) as Double).toInt()
                fullTextDocumentService.documents.values.forEach{d -> validateDocument(d)}
            }
            override fun didChangeWatchedFiles(params: DidChangeWatchedFilesParams) {
                client!!.logMessage(MessageParams(MessageType.Log, "We received an file change event"))
            }
        }
    }

    override fun connect(client: LanguageClient?) {
        this.client = client
    }
}