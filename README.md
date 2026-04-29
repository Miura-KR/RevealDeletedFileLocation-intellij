# Reveal Deleted File Location-intellij

An IntelliJ Platform plugin that helps you navigate to and inspect locally deleted or moved files in the **Project** view.

## Features

### Reveal original location (Commit / Git tool windows)

Context menu actions in:

- The **Commit** tool window (local changes before committing).
- The **Git** tool window's per-commit changed files list (the Log tab's file panel).

Depending on the selected change type, the menu entry is:

- **Reveal Deleted File Location** — shown on a deleted file. Focuses the directory where the file used to be in the Project view.
- **Reveal Pre-Move Location** — shown on a moved / renamed file. Focuses the directory the file was in *before* the move.

If the target directory itself has also been removed, the plugin walks up to the nearest existing ancestor directory.

### Phantom entries in the Project view

Locally deleted or moved files (uncommitted local changes) can be displayed in the **Project** view as phantom entries at their original location:

- Open the Project view tool window's **options menu** (the **⋮** three-dot button on the toolbar) and use the **Reveal Deleted Files** submenu to toggle:
    - **Show Deleted Files** — locally deleted files appear at their original location with strikethrough text. *(Default: ON)*
    - **Show Moved Files (Pre-Move Location)** — locally moved / renamed files appear at their pre-move location with italic text. *(Default: ON)*
- The two toggles are independent and persist per project.
- If the original parent directory no longer exists, the phantom entry attaches to the nearest existing ancestor and shows the original sub-path.
- Phantom entries can be **opened** (Enter or double-click) to view the deleted / pre-move file content as a read-only editor — the same way opening a deleted file from the Git tool window works.
- Right-clicking a phantom entry offers a **Rollback** action (also bound to **Ctrl+Alt+Z**) that restores the file (or undoes the move) via the standard IDE rollback dialog. The selected phantom is pre-checked in the dialog.

The toggles are hidden and phantom entries are not generated for projects without an active VCS.

## Usage

### Reveal original location

1. Open the **Commit** tool window, or the **Git** tool window and select a commit to view its changed files.
2. Right-click on a deleted or moved file entry.
3. Choose **Reveal Deleted File Location** (for deleted files) or **Reveal Pre-Move Location** (for moved files) from the context menu.
4. The Project view opens (if it was closed) and the directory that used to contain the file is selected and focused.

The action only appears when a single deleted or moved change is selected. It is hidden for added / modified files and for multi-selections.

### Phantom entries

1. Make sure the project is under VCS and at least one file is locally deleted or moved.
2. The phantom entry appears in the Project view at its original location (greyed-out, strikethrough for deletions, italic for moves).
3. Press Enter or double-click to view the file's last committed content in a read-only editor.
4. Press **Ctrl+Alt+Z** (or right-click → **Rollback**) to restore the file via the standard rollback dialog.
5. To turn off the display, open the Project view tool window's ⋮ menu → **Reveal Deleted Files** and uncheck the corresponding option.

## Building

This project uses the [IntelliJ Platform Gradle Plugin](https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html).

```
./gradlew buildPlugin     # builds the distributable zip under build/distributions
./gradlew runIde          # launches a sandbox IDE with the plugin installed
./gradlew verifyPlugin    # runs the Plugin Verifier against the target IDE
```

Build settings live in `gradle.properties`:

| Property          | Purpose                                      |
|-------------------|----------------------------------------------|
| `platformVersion` | Target IntelliJ Platform version             |
| `pluginSinceBuild`| Minimum supported IDE build number           |

## Project layout

```
src/main/kotlin/com/k/pmpstudy/
    RevealDeletedFileLocationAction.kt   Reveal-original-location action (Commit / Git tool windows)
    RevealDeletedFilesSettings.kt        Persistent per-project toggle state for phantom entries
    DeletedFileTreeStructureProvider.kt  Injects phantom nodes into the Project view tree
                                         and exposes VcsDataKeys.CHANGES for the selection
    DeletedFileNode.kt                   Phantom node (rendering / open-as-readonly)
    ToggleDeletedFilesActions.kt         Toggle actions for the Project view ⋮ menu
    RollbackDeletedFileAction.kt         Rollback action shown in the Project view popup menu
    RevealDeletedFilesProjectActivity.kt ChangeListListener subscription that refreshes
                                         the Project view when local changes update
src/main/resources/META-INF/
    plugin.xml                           Plugin descriptor / extension & action registration
```

---

# Reveal Deleted File Location-intellij (日本語)

IntelliJ で、ローカルに削除 / 移動されたファイルを「プロジェクト」ビュー上で確認・参照しやすくするプラグインです。

## 機能

### 元の場所をフォーカス(コミット / Git ツールウィンドウ)

以下の場所にコンテキストメニューが追加されます。

- 「コミット」ツールウィンドウ(コミット前のローカル変更一覧)
- 「Git」ツールウィンドウの Log タブで選択したコミットの変更ファイル一覧

選択した Change の種類によってメニュー名が切り替わります。

- **Reveal Deleted File Location**(削除されたファイル向け): 削除されたファイルが元々存在していたディレクトリをフォーカスします。
- **Reveal Pre-Move Location**(移動 / リネームされたファイル向け): 移動前に存在していたディレクトリをフォーカスします。

対象のディレクトリ自体も既に削除されていた場合は、存在している最も近い祖先ディレクトリまで遡ってフォーカスします。

### プロジェクトビューでの擬似エントリ表示

ローカルで削除 / 移動されたファイル(未コミットのローカル変更)を「プロジェクト」ビューに擬似エントリとして元々の場所に表示できます。

- 「プロジェクト」ツールウィンドウの **オプションメニュー**(ツールバー右上の **⋮** ボタン)から **Reveal Deleted Files** サブメニューを開いて、以下を切り替えできます。
    - **Show Deleted Files**: ローカルで削除されたファイルを、元あった場所に取り消し線付きで表示します。*(デフォルト: ON)*
    - **Show Moved Files (Pre-Move Location)**: ローカルで移動 / リネームされたファイルを、移動前の場所に斜体で表示します。*(デフォルト: ON)*
- 2 つのトグルは独立で、プロジェクト単位で永続化されます。
- 元の親ディレクトリも削除されている場合は、存在している最も近い祖先ディレクトリ配下に擬似エントリを置き、元のサブパスを併記して表示します。
- 擬似エントリは Enter キーまたはダブルクリックで**開く**ことができ、Git ツールウィンドウから削除ファイルを開いたときと同様に、削除前 / 移動前のファイル内容が読み取り専用エディタで表示されます。
- 擬似エントリを右クリックすると **Rollback** メニューが表示され(**Ctrl+Alt+Z** にもバインド)、IDE 標準のロールバック確認ダイアログを通じて、削除されたファイルの復元 / 移動を元に戻すことができます。選択中の擬似要素はダイアログ上でチェック済み状態で表示されます。

VCS が有効でないプロジェクトではトグルは非表示になり、擬似エントリも生成されません。

## 使い方

### 元の場所をフォーカス

1. 「コミット」ツールウィンドウ、または「Git」ツールウィンドウで任意のコミットを選択して変更ファイル一覧を表示します。
2. 削除 / 移動されたファイルの行を右クリックします。
3. コンテキストメニューから、削除なら **Reveal Deleted File Location** を、移動なら **Reveal Pre-Move Location** を選択します。
4. 「プロジェクト」ビューが(閉じていた場合は)開き、元のディレクトリが選択・フォーカスされます。

削除または移動された Change が 1 件だけ選ばれているときのみメニューに表示されます。追加 / 変更ファイルや複数選択時には表示されません。

### 擬似エントリ

1. プロジェクトが VCS 配下にあり、ローカルで削除 / 移動されたファイルが少なくとも 1 つあることを確認します。
2. 元の場所に擬似エントリが表示されます(グレーアウト・削除は取り消し線・移動は斜体)。
3. Enter キーまたはダブルクリックで、最後にコミットされていた内容を読み取り専用エディタで開けます。
4. **Ctrl+Alt+Z**(または右クリック → **Rollback**)で標準のロールバックダイアログを開いて復元できます。
5. 表示を OFF にしたい場合は、「プロジェクト」ツールウィンドウの ⋮ メニュー → **Reveal Deleted Files** から該当のチェックを外してください。

## ビルド

[IntelliJ Platform Gradle Plugin](https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html) を使用しています。

```
./gradlew buildPlugin     # build/distributions に配布用 zip を生成
./gradlew runIde          # プラグインを入れたサンドボックス IDE を起動
./gradlew verifyPlugin    # Plugin Verifier で互換性チェック
```

ビルド設定は `gradle.properties` にあります。

| プロパティ         | 用途                                    |
|-------------------|-----------------------------------------|
| `platformVersion` | ターゲットとなる IntelliJ Platform のバージョン |
| `pluginSinceBuild`| サポートする IDE の最小ビルド番号          |

## ディレクトリ構成

```
src/main/kotlin/com/k/pmpstudy/
    RevealDeletedFileLocationAction.kt   元の場所フォーカスアクション(コミット / Git ツールウィンドウ)
    RevealDeletedFilesSettings.kt        擬似エントリ表示トグルの永続化(プロジェクト単位)
    DeletedFileTreeStructureProvider.kt  プロジェクトビューに擬似ノードを注入。
                                         選択中の擬似要素を VcsDataKeys.CHANGES として供給
    DeletedFileNode.kt                   擬似ノード(描画 / 読み取り専用での open)
    ToggleDeletedFilesActions.kt         「プロジェクト」ビュー ⋮ メニュー用トグルアクション
    RollbackDeletedFileAction.kt         擬似要素の右クリックメニューに表示するロールバックアクション
    RevealDeletedFilesProjectActivity.kt ChangeListListener を購読し、ローカル変更更新時に
                                         プロジェクトビューを再描画
src/main/resources/META-INF/
    plugin.xml                           プラグイン定義 / 拡張・アクション登録
```
