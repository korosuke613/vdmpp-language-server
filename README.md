# [WIP] VDM++ Language Server
LSP(Language Server Protocol) for VDM++ implemented in Java using [LSP4J](https://github.com/eclipse/lsp4j).

## build

### build jar
```bash
./gradlew build
```

### run server(standard input mode)
```bash
./gradlew run -q --console=plain --args="-stdio"
```

### run server(client connect mode)
`<PORT>` is the LSP client opened port number.

```bash
./gradlew run -q --args="<PORT>"
```

## Message samples

### initialize

#### send
```json
Content-Length: 3012

{"jsonrpc":"2.0","id":0,"method":"initialize","params":{"processId":59786,"clientInfo":{"name":"vscode","version":"1.42.1"},"rootPath":"/Users/Futa/Desktop/documentation/examples","rootUri":"file:///Users/Futa/Desktop/documentation/examples","capabilities":{"workspace":{"applyEdit":true,"workspaceEdit":{"documentChanges":true,"resourceOperations":["create","rename","delete"],"failureHandling":"textOnlyTransactional"},"didChangeConfiguration":{"dynamicRegistration":true},"didChangeWatchedFiles":{"dynamicRegistration":true},"symbol":{"dynamicRegistration":true,"symbolKind":{"valueSet":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26]}},"executeCommand":{"dynamicRegistration":true},"configuration":true,"workspaceFolders":true},"textDocument":{"publishDiagnostics":{"relatedInformation":true,"versionSupport":false,"tagSupport":{"valueSet":[1,2]}},"synchronization":{"dynamicRegistration":true,"willSave":true,"willSaveWaitUntil":true,"didSave":true},"completion":{"dynamicRegistration":true,"contextSupport":true,"completionItem":{"snippetSupport":true,"commitCharactersSupport":true,"documentationFormat":["markdown","plaintext"],"deprecatedSupport":true,"preselectSupport":true,"tagSupport":{"valueSet":[1]}},"completionItemKind":{"valueSet":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]}},"hover":{"dynamicRegistration":true,"contentFormat":["markdown","plaintext"]},"signatureHelp":{"dynamicRegistration":true,"signatureInformation":{"documentationFormat":["markdown","plaintext"],"parameterInformation":{"labelOffsetSupport":true}},"contextSupport":true},"definition":{"dynamicRegistration":true,"linkSupport":true},"references":{"dynamicRegistration":true},"documentHighlight":{"dynamicRegistration":true},"documentSymbol":{"dynamicRegistration":true,"symbolKind":{"valueSet":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26]},"hierarchicalDocumentSymbolSupport":true},"codeAction":{"dynamicRegistration":true,"isPreferredSupport":true,"codeActionLiteralSupport":{"codeActionKind":{"valueSet":["","quickfix","refactor","refactor.extract","refactor.inline","refactor.rewrite","source","source.organizeImports"]}}},"codeLens":{"dynamicRegistration":true},"formatting":{"dynamicRegistration":true},"rangeFormatting":{"dynamicRegistration":true},"onTypeFormatting":{"dynamicRegistration":true},"rename":{"dynamicRegistration":true,"prepareSupport":true},"documentLink":{"dynamicRegistration":true,"tooltipSupport":true},"typeDefinition":{"dynamicRegistration":true,"linkSupport":true},"implementation":{"dynamicRegistration":true,"linkSupport":true},"colorProvider":{"dynamicRegistration":true},"foldingRange":{"dynamicRegistration":true,"rangeLimit":5000,"lineFoldingOnly":true},"declaration":{"dynamicRegistration":true,"linkSupport":true},"selectionRange":{"dynamicRegistration":true}},"window":{"workDoneProgress":true}},"trace":"off","workspaceFolders":[{"uri":"file:///Users/Futa/Desktop/documentation/examples","name":"examples"}]}}
```

#### receive
```json
Content-Length: 133

{"jsonrpc":"2.0","id":0,"result":{"capabilities":{"textDocumentSync":1,"documentHighlightProvider":true,"codeActionProvider":false}}}
```

### didOpen

#### send
```json
Content-Length: 720

{"jsonrpc":"2.0","method":"textDocument/didOpen","params":{"textDocument":{"uri":"file:///Users/Futa/Desktop/documentation/examples/VDM%2B%2B/MSAWseq/world.vdmpp","languageId":"vdmpp","version":1,"text":"class World\n  \ninstance variables  \n  \npublic static\n  env : [Environment] := nil;\n\npublic static \n  timerRef : Timer := new Timer();    \n\n  \n   \noperations\n\npublic \n  World : () ==> World\n  World() ==\n    ( env := new Environment(\"scenario.txt\");\n      env.setAirSpace(MSAW`airspace);\n      MSAW`atc.addRadar(MSAW`radar1);\n      MSAW`atc.addRadar(MSAW`radar2);\n      MSAW`atc.addObstacle(MSAW`militaryZone);\n    );\n  \npublic \n  Run : () ==> ()\n  Run() ==\n    env.Run();\n\nend World"}}}
```

#### receive
```json
Content-Length: 378

{"jsonrpc":"2.0","method":"textDocument/publishDiagnostics","params":{"uri":"file:///Users/Futa/Desktop/documentation/examples/VDM%2B%2B/MSAWseq/world.vdmpp","diagnostics":[{"range":{"start":{"line":0,"character":0},"end":{"line":0,"character":5}},"message":"Expected \u0027operations\u0027, \u0027state\u0027, \u0027functions\u0027, \u0027types\u0027 or \u0027values\u0027"}]}}
```