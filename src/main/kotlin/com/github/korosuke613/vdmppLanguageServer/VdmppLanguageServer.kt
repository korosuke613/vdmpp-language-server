package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.services.*
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture


class VdmppLanguageServer : LanguageServer, LanguageClientAware {
    private var client: LanguageClient? = null
    private var workspaceRoot: String? = null
    private var maxNumberOfProblems = 100
    //private val logger = LoggerFactory.getLogger(VdmppLanguageServer)

    override fun initialize(params: InitializeParams): CompletableFuture<InitializeResult?>? {
        workspaceRoot = params.rootPath

        // capabilitiesにserverができることを追加していく
        val capabilities = ServerCapabilities()
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full)
        capabilities.setCodeActionProvider(false)
        capabilities.documentHighlightProvider = true
        return CompletableFuture.completedFuture(InitializeResult(capabilities))
    }

    override fun initialized(params: InitializedParams?) {
        client!!.logMessage(MessageParams(MessageType.Info, "hello, world"))
    }

    override fun shutdown(): CompletableFuture<Any?>? {
        return CompletableFuture.completedFuture(null)
    }

    override fun exit() {}

    private val fullTextDocumentService = object : FullTextDocumentService() {
        /**
        DidChangeTextDocumentParams [
          textDocument = VersionedTextDocumentIdentifier [
            version = 2
            uri = "file:///Users/Futa/Desktop/vdmpp-syntax-highlight/vdmfiles/definitions.vdmpp"
          ]
          uri = null
          contentChanges = ArrayList (
            TextDocumentContentChangeEvent [
              range = null
              rangeLength = null
              text = "-- comment\n\nclass うるう年\n\ntypes -- 型定義\n\npublic 年 = nat;\n\nvalues -- 定数定義f\n\npublic static ルール1 : 年 = 4;\npublic static ルール2 : 年 = 100;\npublic static ルール3 : 年 = 400;\n\ninstance variables -- インスタンス変数定義\n\nprivate 西暦 : nat := 2000;\n\noperations -- 操作定義\n\npublic 西暦設定 : 年 ==> int\n  西暦設定(年) == 西暦 := 年;\n\npublic うるう年判定 : () ==> seq of char\n  うるう年判定() ==\n    if(西暦 mod ルール1 = 0) then\n      if(西暦 mod ルール2 = 0) then\n        if(西暦 mod ルール3 = 0) then\n          return "うるう年"\n        else\n          return "平年"\n      else\n        return "うるう年"\n    else\n      return "平年";\n\nend うるう年\n\n\n\n\n\nfunctions\n\npublic うるう年判定仕様 : int -> seq of char\n  うるう年判定仕様(年) ==\n    if(年 mod 4 = 0) then\n      if(年 mod 100 = 0) then\n        if(年 mod 400 = 0) then\n          "うるう年"\n        else\n          "平年"\n      else\n        "うるう年"\n    else\n      "平年";\n\n"
            ]
          )
        ]
        **/
        override fun documentHighlight(position: TextDocumentPositionParams): CompletableFuture<List<DocumentHighlight?>>? {
            super.documentHighlight(position)
            return null
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

        override fun onTypeFormatting(params: DocumentOnTypeFormattingParams): CompletableFuture<List<TextEdit?>>? {
            super.onTypeFormatting(params)
            client!!.logMessage(MessageParams(MessageType.Log, params.toString()))
            return null
        }

        override fun didOpen(params: DidOpenTextDocumentParams) {
            super.didOpen(params)
            val vdmpp: Vdmpp? = this.documents[params.textDocument.uri]
            if (vdmpp != null && client != null) {
                client!!.publishDiagnostics(vdmpp.publishDiagnosticsParams)
            }
        }

        override fun didChange(params: DidChangeTextDocumentParams) {
            super.didChange(params)
            val vdmpp = this.documents[params.textDocument.uri]
            if (vdmpp != null) {
                client!!.publishDiagnostics(vdmpp.publishDiagnosticsParams)
            }
        }
    }

    override fun getTextDocumentService(): TextDocumentService? {
        return fullTextDocumentService
    }

    override fun getWorkspaceService(): WorkspaceService {
        return object : WorkspaceService {
            override fun symbol(params: WorkspaceSymbolParams): CompletableFuture<List<SymbolInformation?>>? {
                return null
            }

            override fun didChangeConfiguration(params: DidChangeConfigurationParams) {
                client!!.logMessage(MessageParams(MessageType.Log, "We received an configuration change event"))
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