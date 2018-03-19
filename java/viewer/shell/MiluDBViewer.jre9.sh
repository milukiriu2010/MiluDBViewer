#!/bin/bash

# ====================================================
# === show warning, when java not found ==============
# ====================================================
notFoundJava()
{
	msg="JDK/JRE9 or later is required!!"
	zenity=`which zenity`
	if [ -n $zenity ]; then
		zenity --warning --text="$msg"
	else
    	echo $msg
	fi
}

# ====================================================
# === check Java version==============================
# ====================================================
checkJavaVersion()
{
	java_version=`$MYJAVA -version 2>&1 | head -1 |  sed -n 's/.*"\(.*\)"/\1/pg'`
    echo "Java Version: "$java_version
	
	#if [[ $java_version =~ \"([[:digit:]])\.([[:digit:]])\.(.*)\" ]]
    #then
    #    JAVA_VER_MAJOR=${BASH_REMATCH[1]}
    #    JAVA_VER_MINOR=${BASH_REMATCH[2]}
    #    JAVA_VER_BUILD=${BASH_REMATCH[3]}
	#
	#	echo "Major: "$JAVA_VER_MAJOR
	#	echo "Minor: "$JAVA_VER_MINOR
	#	echo "Build: "$JAVA_VER_BUILD
    #fi
	JAVA_VER_MAJOR=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\1/p'`
	JAVA_VER_MINOR=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\2/p'`
	JAVA_VER_BUILD=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\3/p'`
	echo "Major: "$JAVA_VER_MAJOR
	echo "Minor: "$JAVA_VER_MINOR
	echo "Build: "$JAVA_VER_BUILD

	if [ "$JAVA_VER_MAJOR" -lt "9" ]; then
      notFoundJava
	fi
}

# ====================================================
# === start application ==============================
# ====================================================
kickJava()
{
	$MYJAVA -classpath $MYCLASSPATH milu.main.MiluDBViewer
	exit
}

# ====================================================
# === cd app path    =================================
# ====================================================
prgdir=`dirname $0`
echo cd $prgdir
cd $prgdir

# ====================================================
# === set class path =================================
# ====================================================
export MYCLASSPATH=$CLASSPATH:.
export MYCLASSPATH=$MYCLASSPATH:$prgdir
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/mysql/mysql-connector-java-5.1.45-bin.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/oracle/ojdbc8.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/oracle/orai18n.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/oracle/xdb6.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/oracle/xmlparserv2.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/postgresql/postgresql-42.1.4.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/cassandra/cassandra-jdbc-driver-0.6.4-shaded.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/jsqlparser/jsqlparser-1.2-SNAPSHOT.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/commons-collections4-4.1.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-schemas-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/xmlbeans-2.6.0.jar

#echo "CLASSPATH=>"
#echo $MYCLASSPATH

# ====================================================
# === set NLS_LANG ===================================
# ====================================================
# export NLS_LANG=French_France.AL32UTF8
# export NLS_LANG=Japanese_Japan.AL32UTF8

# ====================================================
# === start app by java ==============================
# ====================================================
export MYJAVA=`which java`
if [ -n $MYJAVA ]; then 
  checkJavaVersion $MYJAVA
  kickJava
fi

# ====================================================
# === start app by javaw =============================
# ====================================================
export MYJAVA=`which javaw`
if [ -n $MYJAVA ]; then 
  kickJava
fi

# ====================================================
# === Not Found java/javaw ===========================
# ====================================================
notFoundJava

