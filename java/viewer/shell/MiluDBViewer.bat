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
@rem ===  Not Found java/javaw  ===================
@rem ===  or                    ===================
@rem ===  Found under JDK/JRE 9 ===================
@rem ==============================================
:nojava
  @msg "%username%" "JDK/JRE9 or later is required!!"
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
  @if %JAVA_MAJOR_VER% lss 9 goto nojava
  
  @goto exec


@rem ==============================================
@rem === Start App ================================
@rem ==============================================
:exec
  "%MYJAVA%" -classpath %MYCLASSPATH% -jar MiluDBViewer.jar milu.main.MiluDBViewer
  @goto end_batch

:end_batch
  @echo "Batch Done!!"
