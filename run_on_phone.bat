@echo off
cd /d "%~dp0"
echo ========================================================
echo   DANG CAI DAT VA KHOI CHAY CALOOUT LEN DIEN THOAI
echo ========================================================
echo.

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo [*] Downloading gradle-wrapper.jar...
    powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar', 'gradle\wrapper\gradle-wrapper.jar')"
)

echo [*] Installing on connected device...
call gradlew.bat installDebug

if %ERRORLEVEL% equ 0 (
    echo.
    echo [THANH CONG] Da cai dat len dien thoai!
    set "ADB=C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools\adb.exe"
    if exist "%ADB%" (
        echo [*] Launching app...
        "%ADB%" shell am start -n com.caloout.calculator/.MainActivity
    )
) else (
    echo.
    echo [!] Kiem tra: Da bat 'Go loi USB' (USB Debugging) tren dien thoai chua?
)

pause
