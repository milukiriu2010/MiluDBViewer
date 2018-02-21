set MYCLASSPATH=%CLASSPATH%;.;
set MYCLASSPATH=%MYCLASSPATH%;lib/mysql-connector-java-5.1.45-bin.jar;
set MYCLASSPATH=%MYCLASSPATH%;lib/ojdbc8.jar;
set MYCLASSPATH=%MYCLASSPATH%;lib/postgresql-42.1.4.jar;
set MYCLASSPATH=%MYCLASSPATH%;lib/cassandra-jdbc-driver-0.6.4-shaded.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/commons-collections4-4.1.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-schemas-3.17.jar
set MYCLASSPATH=%MYCLASSPATH%;lib/excel/xmlbeans-2.6.0.jar

rem set NLS_LANG=French_France.AL32UTF8
rem set NLS_LANG=Japanese_Japan.AL32UTF8

java -classpath %MYCLASSPATH% milu.main.MiluDBViewer
