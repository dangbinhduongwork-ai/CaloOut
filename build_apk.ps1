# CaloOut - PowerShell Build Script for Android APK
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "      TIEN TRINH BIEN DICH APK CALOOUT TREN MAY" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

Set-Location -Path $PSScriptRoot

# 1. Kiem tra thu vien Gradle Wrapper
$wrapperJar = "gradle\wrapper\gradle-wrapper.jar"
if (-not (Test-Path $wrapperJar)) {
    Write-Host "[*] Dang tai thu vien Gradle Wrapper (lan dau tien)..." -ForegroundColor Yellow
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri "https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar" -OutFile $wrapperJar
    Write-Host "[+] Da tai xong Gradle Wrapper." -ForegroundColor Green
}

# 2. Thuc thi lenh build Gradle
Write-Host ""
Write-Host "[*] Dang bat dau bien dich APK (assembleDebug)..." -ForegroundColor Yellow
Write-Host "[*] Qua trinh co the mat 1-2 phut cho lan build dau tien, vui long doi..." -ForegroundColor Yellow
Write-Host ""

& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host "         [THANH CONG] DA XUAT FILE APK HOAN TAT!" -ForegroundColor Green
    Write-Host "========================================================" -ForegroundColor Green
    
    $sourceApk = "app\build\outputs\apk\debug\app-debug.apk"
    $targetApk = Join-Path $PSScriptRoot "CaloOut.apk"
    
    if (Test-Path $sourceApk) {
        Copy-Item -Path $sourceApk -Destination $targetApk -Force
        Write-Host ""
        Write-Host "File APK da duoc copy ra thu muc goc tai:" -ForegroundColor Green
        Write-Host "  ==> $targetApk" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Dang mo thu muc chua file APK..." -ForegroundColor Yellow
        explorer.exe /select,$targetApk
    }
} else {
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Red
    Write-Host "   CO LOI TRONG QUA TRINH BUILD DONG LENH" -ForegroundColor Red
    Write-Host "========================================================" -ForegroundColor Red
    Write-Host "Ban co the mo Android Studio va bam: Build -> Build APK(s)" -ForegroundColor Yellow
}
