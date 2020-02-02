package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService
import java.util.concurrent.CompletableFuture


/**
 * `TextDocumentService` that only supports `TextDocumentSyncKind.Full` updates.
 * Override members to add functionality.
 */
internal open class FullTextDocumentService : TextDocumentService {
    var documents = HashMap<String, TextDocumentItem>()
    open fun completion(position: TextDocumentPositionParams?): CompletableFuture<CompletionList>? {
        return null
    }

    override fun resolveCompletionItem(unresolved: CompletionItem): CompletableFuture<CompletionItem>? {
        return null
    }

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
        return null
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
        documents[params.textDocument.uri] = params.textDocument
    }

    override fun didChange(params: DidChangeTextDocumentParams) {
        val uri = params.textDocument.uri
        for (changeEvent in params.contentChanges) { // Will be full update because we specified that is all we support
            if (changeEvent.range != null) {
                throw UnsupportedOperationException("Range should be null for full document update.")
            }
            if (changeEvent.rangeLength != null) {
                throw UnsupportedOperationException("RangeLength should be null for full document update.")
            }
            documents[uri]!!.text = changeEvent.text
        }
    }

    override fun didClose(params: DidCloseTextDocumentParams) {
        val uri = params.textDocument.uri
        documents.remove(uri)
    }

    override fun didSave(params: DidSaveTextDocumentParams) {}
}