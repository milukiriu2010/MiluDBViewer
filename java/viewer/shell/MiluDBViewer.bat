@rem ==========================================================
@rem === check JavaFX library path                          ===
@rem === https://openjfx.io/openjfx-docs/#install-javafx    ===
@rem === PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11\lib ===
@rem ==========================================================
@if "%PATH_TO_FX%" == "" goto no_javafx

@rem ============================================
@rem === set classpath ==========================
@rem ============================================
@set MYCLASSPATH=%CLASSPATH%;.;
@set MYCLASSPATH=%MYCLASSPATH%;lib/jsqlparser/jsqlparser-1.2-SNAPSHOT.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/excel/commons-collections4-4.1.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-3.17.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-3.17.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/excel/poi-ooxml-schemas-3.17.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/excel/xmlbeans-2.6.0.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/gson/gson-2.8.3-SNAPSHOT.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/hibernate/hibernate-core-5.3.1.Final.jar
@set MYCLASSPATH=%MYCLASSPATH%;lib/jarchivelib/jarchivelib-0.7.1-jar-with-dependencies.jar
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.base.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.controls.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.fxml.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.graphics.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.media.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.swing.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx.web.jar"
@rem @set MYCLASSPATH=%MYCLASSPATH%;"%PATH_TO_FX%\javafx-swt.jar"

@rem ============================================
@rem === set classpath ==========================
@rem ============================================
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.base/com.sun.javafx.logging=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.font=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.geom.transform=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED 
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.prism=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.prism.paint=ALL-UNNAMED
@set ADDEXPORTS=%ADDEXPORTS% --add-exports javafx.graphics/com.sun.scenario.effect=ALL-UNNAMED

@rem ============================================
@rem === set NLS_LANG ===========================
@rem ============================================
@rem set NLS_LANG=French_France.AL32UTF8
@rem set NLS_LANG=Japanese_Japan.AL32UTF8


@rem ============================================
@rem === start app by java ======================
@rem ============================================
@for %%i in (java.exe) do @set MYJAVA=%%~$PATH:i
@if not "%MYJAVA%" == "" goto java_check

@rem ============================================
@rem === start app by javaw =====================
@rem ============================================
@if "%MYJAVA%" == "" for %%i in (javaw.exe) do @set MYJAVA=%%~$PATH:i

@rem ============================================
@rem === not found "java" and "javaw" ===========
@rem ============================================
@if "%MYJAVA%" == "" goto nojava

@rem ============================================
@rem === start app ==============================
@rem ============================================
@goto exec


@rem ==============================================
@rem ===  Not Found javafx      ===================
@rem ==============================================
:no_javafx
  @msg "%username%" "necessary to set PATH_TO_FX(javafx library path like C:\Program Files\Java\javafx-sdk-12\lib). see https://openjfx.io/openjfx-docs/#install-javafx"
  @goto end_batch


@rem ===============================================
@rem ===  Not Found java/javaw   ===================
@rem ===  or                     ===================
@rem ===  Found under JDK/JRE 12 ===================
@rem ===============================================
:nojava
  @msg "%username%" "JDK/JRE12 or later is required!!"
  @goto end_batch

@rem ==============================================
@rem == Java Version Check ========================
@rem https://stackoverflow.com/questions/5675459/how-to-get-java-version-from-batch-script
@rem ==============================================
:java_check
  @for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do @(
    @set JAVAVER=%%g
  )
  @set JAVAVER=%JAVAVER:"=%
  @echo Java Version: %JAVAVER%
  
  @for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do @(
    @echo Major: %%v
    @echo Minor: %%w
    @echo Build: %%x
    @set JAVA_MAJOR_VER=%%v
  )
  @if %JAVA_MAJOR_VER% lss 12 goto nojava
  
  @goto exec


@rem ==========================================================
@rem === Start App ============================================
@rem ==========================================================
:exec
  "%MYJAVA%" --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.web %ADDEXPORTS% -classpath %MYCLASSPATH% -jar MiluDBViewer.jar milu.main.MiluDBViewer
  @goto end_batch

:end_batch
  @echo "Batch Done!!"
