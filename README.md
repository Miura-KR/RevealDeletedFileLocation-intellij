# Reveal Deleted File Location-intellij

An IntelliJ Platform plugin that adds context menu actions for navigating to the original location of deleted or moved files.

The actions are available in:

- The **Commit** tool window (local changes before committing).
- The **Git** tool window's per-commit changed files list (the Log tab's file panel).

Depending on the selected change type, the menu entry is:

- **Reveal Deleted File Location** — shown on a deleted file. Focuses the directory where the file used to be in the Project view.
- **Reveal Pre-Move Location** — shown on a moved / renamed file. Focuses the directory the file was in *before* the move.

If the target directory itself has also been removed, the plugin walks up to the nearest existing ancestor directory.

## Usage

1. Open the **Commit** tool window, or the **Git** tool window and select a commit to view its changed files.
2. Right-click on a deleted or moved file entry.
3. Choose **Reveal Deleted File Location** (for deleted files) or **Reveal Pre-Move Location** (for moved files) from the context menu.
4. The Project view opens (if it was closed) and the directory that used to contain the file is selected and focused.

The action only appears when a single deleted or moved change is selected. It is hidden for added / modified files and for multi-selections.

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
    RevealDeletedFileLocationAction.kt   Action implementation
src/main/resources/META-INF/
    plugin.xml                           Plugin descriptor / action registration
```

---

# Reveal Deleted File Location-intellij (日本語)

IntelliJ で、削除または移動されたファイルが元々存在していたディレクトリを「プロジェクト」ビューでフォーカスするコンテキストメニューを追加するプラグインです。

メニューは以下の場所に表示されます。

- 「コミット」ツールウィンドウ(コミット前のローカル変更一覧)
- 「Git」ツールウィンドウの Log タブで選択したコミットの変更ファイル一覧

選択した Change の種類によってメニュー名が切り替わります。

- **Reveal Deleted File Location**(削除されたファイル向け): 削除されたファイルが元々存在していたディレクトリをフォーカスします。
- **Reveal Pre-Move Location**(移動 / リネームされたファイル向け): 移動前に存在していたディレクトリをフォーカスします。

対象のディレクトリ自体も既に削除されていた場合は、存在している最も近い祖先ディレクトリまで遡ってフォーカスします。

## 使い方

1. 「コミット」ツールウィンドウ、または「Git」ツールウィンドウで任意のコミットを選択して変更ファイル一覧を表示します。
2. 削除 / 移動されたファイルの行を右クリックします。
3. コンテキストメニューから、削除なら **Reveal Deleted File Location** を、移動なら **Reveal Pre-Move Location** を選択します。
4. 「プロジェクト」ビューが(閉じていた場合は)開き、元のディレクトリが選択・フォーカスされます。

削除または移動された Change が 1 件だけ選ばれているときのみメニューに表示されます。追加 / 変更ファイルや複数選択時には表示されません。

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
    RevealDeletedFileLocationAction.kt   アクションの実装
src/main/resources/META-INF/
    plugin.xml                           プラグイン定義 / アクション登録
```
