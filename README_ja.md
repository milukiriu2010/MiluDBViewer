[English](README.md)

# 概要

- DB上のデータ閲覧・SQLコマンドを発行可能なGUIツール
- **サポートしているDB: Oracle, PostgreSQL, MySQL, Cassandra, SQLite, SQLServer**. 将来的には、JDBCに準拠しているDBを、もっと追加する予定です。
- マルチ プラットフォーム: Windows, Linux, MacOS.
- マルチ　言語: 日本語・英語・フランス語・スペイン語・中国語.
- フリー＆オープンソース.

# ダウンロード

[https://sourceforge.net/projects/miludbviewer/files/?source=navbar](https://sourceforge.net/projects/miludbviewer/files/?source=navbar)

# インストールと起動

1. このアプリは JDK 10以上を必要としています.
   [JDK 10 ダウンロード - Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)

2-1. Windows
(1) "MiluDBViewer_Setup*.*.*.exe"を実行してインストールしてください。
(2) "<<your path>>\MiluDBViewer.exe" を実行してください。

2-2. Linux
(1) MiluDBViewer*.*.*.tgzを解凍してください。
(2) $ cd <<your path>>
(3) $ ./MiluDBViewer***.sh を実行してください。

# ポイント

## 縦方向のデータコピー
自身で作成したアプリケーションのデータ遷移をチェックするために用います。
たくさんのカラムがあるテーブル上のデータをみるのに、よいかと思います。

1. 縦方向にデータコピー
![alt tag](doc/en/c01.copy_01.png)

2. Excel, Open Office, Googleスプレッドシートのような表計算アプリに貼り付けます
![alt tag](doc/en/c01.copy_02_excel.png)

## スクリプトの実行

1. "Result"タブに全SQLの実行結果が表示されます
![alt tag](doc/en/s01.01result.png)

2. "Script#"タブに各SQLの実行結果が表示されます 
![alt tag](doc/en/s01.02script7.png)

3. 選択されたSQLのみの実行も可能です
![alt tag](doc/en/s01.03result_single.png)

## 補完

1. "."を契機に補完候補を表示します
![alt tag](doc/en/c02.completion.png)

## スキーマ表示

1. スキーマがもつデータを表示します。現バージョンでは、"表・ビュー => 定義. プロシージャ => ソース"を表示します.
![alt tag](doc/en/c03.schema_browse.png)

サポートしているオブジェクト一覧

x|Table|View|Materialized View|Function|Aggregate|Procedure|Package|Type|Trigger|Sequence
-|-----|----|-----------------|--------|---------|---------|-------|----|-------|--------
Cassandra|〇|×|〇|〇|〇|×|×|〇|×|×
MongoDB|〇|×|×|×|×|×|×|×|×|×
MySQL|〇|〇|×|〇|×|〇|×|〇|×
Oracle|〇|〇|〇|〇|×|〇|〇|〇|〇|〇
PostgreSQL|〇|〇|〇|〇|×|×|×|〇|〇|〇
SQLite|〇|〇|×|×|×|×|×|×|×|×
SQLServer|〇|〇|×|〇|×|〇|×|〇|〇|〇

## ER図

1. 外部キーをたどっていくことでER図を作成しています
![alt tag](doc/en/c04.er_diagram.png)

# DB接続の設定

- [Cassandra](doc/ja/START_Cassandra.md)
- [MongoDB](doc/ja/START_MongoDB.md)
- [MySQL](doc/ja/START_MySQL.md)
- [Oracle](doc/ja/START_Oracle.md)
- [PostgreSQL](doc/ja/START_PostgreSQL.md)
- [SQLite](doc/ja/START_SQLite.md)
- [SQLServer](doc/ja/START_SQLServer.md)

