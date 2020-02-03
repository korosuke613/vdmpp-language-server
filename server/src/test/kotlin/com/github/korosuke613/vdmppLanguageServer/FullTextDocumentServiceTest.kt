package com.github.korosuke613.vdmppLanguageServer

import org.eclipse.lsp4j.DidOpenTextDocumentParams
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.TextDocumentItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FullTextDocumentServiceTest {
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
    private val fullTextDocumentService = FullTextDocumentService()

    @Test
    fun createHighlights() {
        val textDocument = TextDocumentItem()
        textDocument.uri = "file:///Users/Futa/Desktop/vdmpp-syntax-highlight/vdmfiles/definitions.vdmpp"
        textDocument.text = "-- comment\n\nclass うるう年\n\ntypes -- 型定義\n\npublic 年 = nat;\n\nvalues -- 定数定義f\n\npublic static ルール1 : 年 = 4;\npublic static ルール2 : 年 = 100;\npublic static ルール3 : 年 = 400;\n\ninstance variables -- インスタンス変数定義\n\nprivate 西暦 : nat := 2000;\n\noperations -- 操作定義\n\npublic 西暦設定 : 年 ==> int\n  西暦設定(年) == 西暦 := 年;\n\npublic うるう年判定 : () ==> seq of char\n  うるう年判定() ==\n    if(西暦 mod ルール1 = 0) then\n      if(西暦 mod ルール2 = 0) then\n        if(西暦 mod ルール3 = 0) then\n          return \"うるう年\"\n        else\n          return \"平年\"\n      else\n        return \"うるう年\"\n    else\n      return \"平年\";\n\nend うるう年\n\n\n\n\n\nfunctions\n\npublic うるう年判定仕様 : int -> seq of char\n  うるう年判定仕様(年) ==\n    if(年 mod 4 = 0) then\n      if(年 mod 100 = 0) then\n        if(年 mod 400 = 0) then\n          \"うるう年\"\n        else\n          \"平年\"\n      else\n        \"うるう年\"\n    else\n      \"平年\";\n\n"
        val params = DidOpenTextDocumentParams(textDocument)
        fullTextDocumentService.didOpen(params)
        val position = Position(11, 15)
        fullTextDocumentService.createHighlights(textDocument.uri, position)
        assert(true)
    }
}