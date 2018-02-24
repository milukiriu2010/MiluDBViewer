set MYCLASSPATH=%CLASSPATH%;.;
set MYCLASSPATH=%MYCLASSPATH%;lib/mysql/mysql-connector-java-5.1.45-bin.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/oracle/ojdbc8.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/oracle/xdb6.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/oracle/xmlparserv2.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/postgresql/postgresql-42.1.4.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/cassandra/cassandra-jdbc-driver-0.6.4-shaded.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/jsqlparser/jsqlparser-1.2-SNAPSHOT.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/commons-collections4-4.1.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-schemas-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/xmlbeans-2.6.0.jar

rem set NLS_LANG=French_France.AL32UTF8
rem set NLS_LANG=Japanese_Japan.AL32UTF8

java -classpath %MYCLASSPATH% milu.main.MiluDBViewer
