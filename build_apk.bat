@echo off
chcp 65001 >nul
title CaloOut - Tiến trình tự động xuất file APK Android

echo ========================================================
echo       BẮT ĐẦU QUÁ TRÌNH TỰ ĐỘNG XUẤT FILE APK CALOOUT
echo ========================================================
echo.

cd /d "%~dp0"

:: 1. Kiểm tra & cấu hình JAVA_HOME tự động nếu chưa có
if defined JAVA_HOME goto check_sdk

echo [*] Đang tìm kiếm JDK trên máy...
if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    goto check_sdk
)
if exist "C:\Program Files\Android\Android Studio\jre" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jre"
    goto check_sdk
)
for /d %%i in ("C:\Program Files\Java\jdk-*") do (
    if exist "%%i" set "JAVA_HOME=%%i"
)
if defined JAVA_HOME goto check_sdk

for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-*") do (
    if exist "%%i" set "JAVA_HOME=%%i"
)

:check_sdk
if defined JAVA_HOME (
    echo [+] Đã tìm thấy Java tại: "%JAVA_HOME%"
    set "PATH=%JAVA_HOME%\bin;%PATH%"
) else (
    echo [!] Chưa tìm thấy Java trong biến môi trường.
)

:: 2. Kiểm tra gradle-wrapper.jar
if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo [*] Đang chuẩn bị thư viện Gradle Wrapper...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar', 'gradle\wrapper\gradle-wrapper.jar')" 2>nul
)

:: 3. Tiến hành biên dịch APK
echo.
echo [*] Đang thực thi biên dịch: gradlew.bat assembleDebug...
echo [*] Vui lòng đợi trong giây lát...
echo.

call "gradlew.bat" assembleDebug

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================================
    echo   [THÀNH CÔNG] ĐÃ XUẤT FILE APK HOÀN TẤT!
    echo ========================================================
    
    if exist "app\build\outputs\apk\debug\app-debug.apk" (
        copy /y "app\build\outputs\apk\debug\app-debug.apk" "CaloOut.apk" >nul
        echo.
        echo File APK đã được copy trực tiếp ra thư mục gốc:
        echo   ==> %~dp0CaloOut.apk
        echo.
        echo Đang mở thư mục chứa file APK...
        explorer.exe /select,"%~dp0CaloOut.apk"
    )
) else (
    echo.
    echo ========================================================
    echo   [CHÚ Ý] KHÔNG THỂ BIÊN DỊCH TRỰC TIẾP BẰNG DÒNG LỆNH
    echo ========================================================
    echo Nguyên nhân: Máy tính của bạn có thể chưa cấu hình Java JDK
    echo hoặc các công cụ Android SDK Command-line.
    echo.
    echo => GIẢI PHÁP ĐƠN GIẢN NHẤT:
    echo 1. Mở phần mềm Android Studio.
    echo 2. Chọn File -> Open -> Chọn thư mục này (%~dp0).
    echo 3. Nhìn lên Menu chọn: Build -> Build Bundle(s) / APK(s) -> Build APK(s).
    echo.
)

echo.
pause
