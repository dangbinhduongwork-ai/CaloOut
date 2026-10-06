@echo off
cd /d "%~dp0"
echo ========================================================
echo        DANG BIEN DICH APK CALOOUT TREN WINDOWS
echo ========================================================
echo.

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo [*] Downloading gradle-wrapper.jar...
    powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar', 'gradle\wrapper\gradle-wrapper.jar')"
)

echo [*] Executing gradlew.bat assembleDebug...
echo [*] Vui long doi trong giay lat...
echo.

call gradlew.bat assembleDebug

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    copy /y "app\build\outputs\apk\debug\app-debug.apk" "CaloOut.apk" >nul
    echo.
    echo ========================================================
    echo      [THANH CONG] DA TAO XONG FILE CALOOUT.APK!
    echo ========================================================
    echo File APK nam tai: %~dp0CaloOut.apk
    echo.
    explorer.exe /select,"%~dp0CaloOut.apk"
) else (
    echo.
    echo [!] Build that bai hoac chua hoan tat.
)

pause
