# SQLServerへ接続するときの設定

1. SQLServer用JDBCをダウンロード. [[https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-2017](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-2017).

2. MiluDBViewerを起動. "JDBC追加"をクリック.

![alt tag](a01.start_add_driver.png)

3. "JDBCドライバ パス(.jar)"に追加. "JDBCドライバ クラス名"に"com.microsoft.sqlserver.jdbc.SQLServerDriver"を入力. その他の項目は任意入力です. "ロード"をクリック.
![alt tag](a02.add_driver_SQLServer.png)

4. 接続アイコンをブックマーク上に作成します. "SQLServer"を選択、接続先データベース/ホスト名を入力し、"接続"をクリック.
![alt tag](a03.connect_SQLServer.png)
