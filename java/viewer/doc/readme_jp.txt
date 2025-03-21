============================================================
1. ソフトの概要
============================================================
MiluDBViewerは、データベースのGUIツールで、フリーソフトです。

============================================================
2. 動作確認したDB/OS
============================================================
(1) DB
Cassandra 3.9.0
MariaDB 10.3
MySQL 8.0.11/5.7.20
Oracle 12c Release2/11g Release2
PostgreSQL 10.4/10.1
SQLite 3.23.1
Microsoft SQLServer 2017
MongoDB 3.6.5

(2) OS
Windows 10
Ubuntu 18.04/16.04
mac OS High Sierra version 10.13.1

============================================================
3. 必要な環境
============================================================
JRE/JDK 10.0.1以上
+ JavaFX

**************************************************
* JavaFX                                         *
**************************************************
JavaFX は jdk11 以降含まれていません。
https://openjfx.io/openjfx-docs/#install-java
https://openjfx.io/openjfx-docs/#install-javafx

javafxライブラリへのパスが必要になります。
例：
PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11\lib

============================================================
4. インストールと実行
============================================================
4-1. Windows
(1) "MiluDBViewer_Setup*.*.*.exe"を起動してください。
(2) "<<インストールパス>>\MiluDBViewer.exe"を起動してください。

4-2. Linux & Mac
(1) MiluDBViewer*.*.*.tgzを解凍ソフトで解凍してください。
(2) $ cd <<インストールパス>>
(3) $ ./MiluDBViewer***.sh


============================================================
5. アンインストール
============================================================
5-1. Windows
(1) [コントロールパネル]-[プログラムと機能]を開いてください。
(2) "MiluDBViewer"を選択し、アンインストールしてください。

5-2. Linux & Mac
(1) rm -rf <<インストールパス>>

============================================================
6. 連絡先とヘルプ
============================================================
https://github.com/milukiriu2010/MiluDBViewer
https://sourceforge.net/projects/miludbviewer/
milu.kiriu2010@gmail.com
