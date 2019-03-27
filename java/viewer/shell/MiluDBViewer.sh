#!/bin/bash

# ====================================================
# === show warning, when java not found ==============
# ====================================================
notFoundJava()
{
	msg="JDK12 or later is required!!"
	zenity=`which zenity`
	if [ -n $zenity ]; then
		zenity --warning --text="$msg"
	else
    	echo $msg
	fi
	exit
}

# ====================================================
# === check Java version==============================
# ====================================================
checkJavaVersion()
{
	# java version "10.0.1" 2018-04-17
	#java_version=`$MYJAVA -version 2>&1 | head -1 |  sed -n 's/.*"\(.*\)"/\1/pg'`
	# openjdk version "11" 2018-09-25
	java_version=`$MYJAVA -version 2>&1 | head -1 |  sed -n 's/.*"\(.*\)".*/\1/pg'`
    echo "Java Version: ["$java_version"]"
	
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
	#JAVA_VER_MAJOR=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\1/p'`
	#JAVA_VER_MINOR=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\2/p'`
	#JAVA_VER_BUILD=`echo $java_version | sed -n 's/^\(.*\)\.\(.*\)\.\(.*\)$/\3/p'`
	JAVA_VER_MAJOR=`echo $java_version | sed -n 's/^\(.*\)\.*\(.*\)\.*\(.*\)$/\1/p'`
	JAVA_VER_MINOR=`echo $java_version | sed -n 's/^\(.*\)\.*\(.*\)\.*\(.*\)$/\2/p'`
	JAVA_VER_BUILD=`echo $java_version | sed -n 's/^\(.*\)\.*\(.*\)\.*\(.*\)$/\3/p'`
	echo "Major: "$JAVA_VER_MAJOR
	echo "Minor: "$JAVA_VER_MINOR
	echo "Build: "$JAVA_VER_BUILD

	if [ $JAVA_VER_MAJOR -lt 12 ]; then
      notFoundJava
	fi
}

# ==========================================================
# === start application ====================================
# === https://openjfx.io/openjfx-docs/#install-javafx    ===
# === PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11\lib ===
# ==========================================================
kickJava()
{
	$MYJAVA --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.web -classpath $MYCLASSPATH $ADDEXPORTS -jar MiluDBViewer.jar milu.main.MiluDBViewer
	exit
}

# ==============================================
# ===  Not Found javafx      ===================
# ==============================================
noJavaFx()
{
	msg="necessary to set PATH_TO_FX(javafx library path like C:\Program Files\Java\javafx-sdk-12\lib). see https://openjfx.io/openjfx-docs/#install-javafx"
	zenity=`which zenity`
	if [ -n $zenity ]; then
		zenity --warning --text="$msg"
	else
    	echo $msg
	fi
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
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/jsqlparser/jsqlparser-1.2-SNAPSHOT.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/commons-collections4-4.1.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/poi-ooxml-schemas-3.17.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/excel/xmlbeans-2.6.0.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/gson/gson-2.8.3-SNAPSHOT.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/hibernate/hibernate-core-5.3.1.Final.jar
export MYCLASSPATH=$MYCLASSPATH:$prgdir/lib/jarchivelib/jarchivelib-0.7.1-jar-with-dependencies.jar

#echo "CLASSPATH=>"
#echo $MYCLASSPATH

# ============================================
# === set classpath ==========================
# ============================================
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.base/com.sun.javafx.logging=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.font=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.geom.transform=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.prism=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.prism.paint=ALL-UNNAMED"
export ADDEXPORTS="$ADDEXPORTS --add-exports javafx.graphics/com.sun.scenario.effect=ALL-UNNAMED"


# ====================================================
# === set NLS_LANG ===================================
# ====================================================
# export NLS_LANG=French_France.AL32UTF8
# export NLS_LANG=Japanese_Japan.AL32UTF8

# ==========================================================
# === check JavaFX library path                          ===
# === https://openjfx.io/openjfx-docs/#install-javafx    ===
# === PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11\lib ===
# ==========================================================
if [ -z $PATH_TO_FX ]; then
  noJavaFx
fi

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

