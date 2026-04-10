# Reveal Deleted File Location-intellij

An IntelliJ Platform plugin that adds an **Reveal Deleted File Location** action to the Commit tool window context menu for deleted files.

When invoked on a deleted file, the action focuses the directory where the file was located in the Project view. If that directory itself has also been removed, the plugin walks up to the nearest existing ancestor directory.

## Usage

1. Open the **Commit** tool window (the tool window that lists local changes before committing).
2. Right-click on a deleted file entry.
3. Choose **Reveal Deleted File Location** from the context menu.
4. The Project view opens (if it was closed) and the directory that used to contain the file is selected and focused.

The action only appears when a single deleted change is selected. It is hidden for added / modified files and for multi-selections.

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

IntelliJ の「コミット」ツールウィンドウで、削除されたファイルを右クリックしたときのコンテキストメニューに **Reveal Deleted File Location** を追加するプラグインです。

このメニューを実行すると、削除されたファイルが元々存在していたディレクトリを「プロジェクト」ビューでフォーカスします。ディレクトリ自体も削除されていた場合は、存在している最も近い祖先ディレクトリまで遡ってフォーカスします。

## 使い方

1. 「コミット」ツールウィンドウを開きます。
2. 削除されたファイルの行を右クリックします。
3. コンテキストメニューから **Reveal Deleted File Location** を選択します。
4. 「プロジェクト」ビューが(閉じていた場合は)開き、元のディレクトリが選択・フォーカスされます。

削除された Change が 1 件だけ選ばれているときのみメニューに表示されます。追加 / 変更ファイルや複数選択時には表示されません。

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
