; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "MiluDBViewer"
#define MyAppVersion "0.4.0_jdk24"
#define MyAppPublisher "Milu"
#define MyAppURL "https://github.com/milukiriu2010/MiluDBViewer"
#define MyAppExeName "MiluDBViewer.bat"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{4D21A2DE-6AE1-4B06-BC2E-62471FDA1628}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DisableProgramGroupPage=yes
LicenseFile=F:\myjava\MiluDBViewer.git\java\viewer\build\doc\license\MiluDBViewer\license.txt
OutputDir=F:\myjava\MiluDBViewer.git\java\viewer\exe
OutputBaseFilename=MiluDBViewer_Setup
SetupIconFile=F:\myjava\MiluDBViewer.git\java\viewer\resources\images\winicon.ico
Compression=lzma
SolidCompression=yes
; UninstallDisplayIcon={app}\MiluDBViewer.exe

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"
Name: "japanese"; MessagesFile: "compiler:Languages\Japanese.isl"
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 0,6.1

[Files]
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\MiluDBViewer.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\MiluDBViewer.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\conf\*"; DestDir: "{app}\conf"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\doc\*"; DestDir: "{app}\doc"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "F:\myjava\MiluDBViewer.git\java\viewer\build\resources\*"; DestDir: "{app}\resources"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{commonprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Code]
//function InitializeUninstall(): Boolean;
//begin
//  Result := MsgBox('InitializeUninstall:' #13#13 'Uninstall is initializing. Do you really want to start Uninstall?', mbConfirmation, MB_YESNO) = idYes;
//  if Result = False then
//    MsgBox('InitializeUninstall:' #13#13 'Uninstall is canceled.', mbInformation, MB_OK);
//end;


procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
var
  UserDir: Variant;
  DeleteAccept: Boolean;
  DeleteOK: Boolean;
begin
  case CurUninstallStep of
    usUninstall:
      begin
        UserDir := ExpandConstant('{%USERPROFILE}') + '\.MiluDBViewer'
        if ( DirExists(UserDir) ) = True then
          DeleteAccept := MsgBox('Delete user data folder:' #13#13 'Do you want to remove the user data(' + UserDir + ')?', mbConfirmation, MB_YESNO) = idYes;
          if DeleteAccept = True then
            DelTree( UserDir, True, True, True );
            if DeleteOK = False then
              MsgBox('Delete user data folder Failed(' + UserDir + ').', mbInformation, MB_OK);
      end;
  end;
end;


