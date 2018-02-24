#!/bin/sh

prgdir=`dirname $0`
echo cd $prgdir
cd $prgdir

export MYCLASSPATH=$CLASSPATH:.
export MYCLASSPATH=$MYCLASSPATH:$prgdir
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/mysql-connector-java-5.1.45-bin.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/ojdbc8.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/postgresql-42.1.4.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/cassandra-jdbc-driver-0.6.4-shaded.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/jsqlparser/jsqlparser-1.2-SNAPSHOT.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/commons-collections4-4.1.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-schemas-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/xmlbeans-2.6.0.jar

echo "CLASSPATH=>"
echo $MYCLASSPATH

# export NLS_LANG=French_France.AL32UTF8
# export NLS_LANG=Japanese_Japan.AL32UTF8

java -classpath $MYCLASSPATH milu.main.MiluDBViewer
