package com.github.korosuke613.vdmppLanguageServer

import com.fujitsu.vdmj.ast.definitions.ASTDefinitionList
import com.fujitsu.vdmj.lex.Dialect
import com.fujitsu.vdmj.lex.LexTokenReader
import com.fujitsu.vdmj.syntax.DefinitionReader
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService
import java.util.concurrent.CompletableFuture


/**
 * `TextDocumentService` that only supports `TextDocumentSyncKind.Full` updates.
 * Override members to add functionality.
 */
internal open class FullTextDocumentService : TextDocumentService {
    var documents = HashMap<String, Vdmpp>()

    override fun hover(position: TextDocumentPositionParams): CompletableFuture<Hover>? {
        return null
    }

    override fun signatureHelp(position: TextDocumentPositionParams): CompletableFuture<SignatureHelp>? {
        return null
    }

    override fun definition(position: TextDocumentPositionParams): CompletableFuture<Either<MutableList<out Location>, MutableList<out LocationLink>>>? {
        return null
    }

    override fun references(params: ReferenceParams): CompletableFuture<List<Location?>>? {
        return null
    }

    override fun documentHighlight(position: TextDocumentPositionParams): CompletableFuture<List<DocumentHighlight?>>? {
        /*
        TextDocumentPositionParams [
          textDocument = TextDocumentIdentifier [
            uri = "file:///Users/Futa/Desktop/vdmpp-syntax-highlight/vdmfiles/definitions.vdmpp"
          ]
          uri = null
          position = Position [
            line = 16
            character = 24
          ]
        ]
         */
        val highlights = createHighlights(position.textDocument.uri, position.position)
        println(highlights)
        return null
    }

    fun createHighlights(uri: String, position: Position): String {
        val document = documents[uri]
        return "uri:${uri}, position:${position}"
    }

    override fun documentSymbol(params: DocumentSymbolParams): CompletableFuture<MutableList<Either<SymbolInformation, DocumentSymbol>>>? {
        return null
    }

    override fun codeAction(params: CodeActionParams): CompletableFuture<MutableList<Either<Command, CodeAction>>>? {
        return null
    }

    override fun codeLens(params: CodeLensParams): CompletableFuture<List<CodeLens>>? {
        return null
    }

    override fun resolveCodeLens(unresolved: CodeLens): CompletableFuture<CodeLens>? {
        return null
    }

    override fun formatting(params: DocumentFormattingParams): CompletableFuture<List<TextEdit?>>? {
        // "-- comment\n\nclass うるう年\n\ntypes -- 型定義\n\npublic 年 = nat;\n\nvalues -- 定数定義f\n\npublic static ルール1 : 年 = 4;\npublic static ルール2 : 年 = 100;\npublic static ルール3 : 年 = 400;\n\ninstance variables -- インスタンス変数定義\n\nprivate 西暦 : nat := 2000;\n\noperations -- 操作定義\n\npublic 西暦設定 : 年 ==> int\n  西暦設定(年) == 西暦 := 年;\n\npublic うるう年判定 : () ==> seq of char\n  うるう年判定() ==\n    if(西暦 mod ルール1 = 0) then\n      if(西暦 mod ルール2 = 0) then\n        if(西暦 mod ルール3 = 0) then\n          return "うるう年"\n        else\n          return "平年"\n      else\n        return "うるう年"\n    else\n      return "平年";\n\nend うるう年\n\n\n\n\n\nfunctions\n\npublic うるう年判定仕様 : int -> seq of char\n  うるう年判定仕様(年) ==\n    if(年 mod 4 = 0) then\n      if(年 mod 100 = 0) then\n        if(年 mod 400 = 0) then\n          "うるう年"\n        else\n          "平年"\n      else\n        "うるう年"\n    else\n      "平年";\n\n"
        return null
    }

    override fun rangeFormatting(params: DocumentRangeFormattingParams): CompletableFuture<List<TextEdit?>>? {
        return null
    }

    override fun onTypeFormatting(params: DocumentOnTypeFormattingParams): CompletableFuture<List<TextEdit?>>? {
        return null
    }

    override fun rename(params: RenameParams): CompletableFuture<WorkspaceEdit>? {
        return null
    }

    override fun didOpen(params: DidOpenTextDocumentParams) {
        val vdmpp = Vdmpp(params.textDocument)
        documents[params.textDocument.uri] = vdmpp
    }

    override fun didChange(params: DidChangeTextDocumentParams) {
        val vdmpp = documents[params.textDocument.uri]
        if(vdmpp != null) {
            vdmpp.textDocumentItem.text = params.contentChanges[0].text
            vdmpp.updateVdmppFile()
        }
    }

    override fun didClose(params: DidCloseTextDocumentParams) {
        val uri = params.textDocument.uri
        documents.remove(uri)
    }

    override fun didSave(params: DidSaveTextDocumentParams) {}
}