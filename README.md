[日本語](README_ja.md)

# Overview

- GUI Tool to browse data, issue SQL commands on DB.
- **Supported DB: Oracle, PostgreSQL, MySQL, Cassandra, SQLite, SQLServer**. Going to add more JDBC compliant DB in the future.
- Multi Platform: Windows, Linux, MacOS.
- Multi Language: English, French, Spanish, Chinese, Japanse.
- Free and Open-Source.

# Download

[https://sourceforge.net/projects/miludbviewer/files/?source=navbar](https://sourceforge.net/projects/miludbviewer/files/?source=navbar)

# Install & Run

1. This app requires JDK 10 or later.
   [JDK 10 Download from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)

2-1. Windows
(1) run "MiluDBViewer_Setup*.*.*.exe"
(2) run "<<your path>>\MiluDBViewer.exe"

2-2. Linux
(1) extract MiluDBViewer*.*.*.tgz by a file archiver to <<your path>>
(2) $ cd <<your path>>
(3) $ ./MiluDBViewer***.sh

# Checked DB
(1) Cassandra 3.9.0
(2) MySQL 5.7.20
(3) Oracle 12c Release2/11g Release2
(4) PostgreSQL 10.4/10.1
(5) SQLite 3.23.1
(6) Microsoft SQLServer 2017
(7) MongoDB 3.6.5

# Features

## Copy data vertically
This is the feature that I want and use the most.
Enable to check data transition by your app.
Especially for tables having many columns.

1. Copy data vertically
![alt tag](doc/en/c01.copy_01.png)

2. Paste it to a spreadsheet app like Excel, Open Office, Google spreadsheet.
![alt tag](doc/en/c01.copy_02_excel.png)

## Run Script

1. All SQL Results on "Result" Tab.
![alt tag](doc/en/s01.01result.png)

2. Each SQL Result on "Script#" Tab.
![alt tag](doc/en/s01.02script7.png)

3. Execute selected SQL.
![alt tag](doc/en/s01.03result_single.png)

## Completion

1. List popup for competion after "."
![alt tag](doc/en/c02.completion.png)

## Schema Browse

1. Browse schema objects. Table and View => definition. Procedure => Source.
![alt tag](doc/en/c03.schema_browse.png)

Supported Objects

x|Table|View|Materialized View|Function|Aggregate|Procedure|Package|Type|Trigger|Sequence
-|-----|----|-----------------|--------|---------|---------|-------|----|-------|--------
Cassandra|〇|×|〇|〇|〇|×|×|〇|×|×
MongoDB|〇|×|×|×|×|×|×|×|×|×
MySQL|〇|〇|×|〇|×|〇|×|〇|×
Oracle|〇|〇|〇|〇|×|〇|〇|〇|〇|〇
PostgreSQL|〇|〇|〇|〇|×|×|×|〇|〇|〇
SQLite|〇|〇|×|×|×|×|×|×|×|×
SQLServer|〇|〇|×|〇|×|〇|×|〇|〇|〇

## ER Diagram

1. Draw ER diagram through traversing foreign keys.
![alt tag](doc/en/c04.er_diagram.png)

# Configuration to connect to DB

- [Cassandra](doc/en/START_Cassandra.md)
- [MongoDB](doc/en/START_MongoDB.md)
- [MySQL](doc/en/START_MySQL.md)
- [Oracle](doc/en/START_Oracle.md)
- [PostgreSQL](doc/en/START_PostgreSQL.md)
- [SQLite](doc/en/START_SQLite.md)
- [SQLServer](doc/en/START_SQLServer.md)

<hr>

[GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

