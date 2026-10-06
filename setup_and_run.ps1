[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"

Set-Location -Path "e:\23.app caloout\CaloOut"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   TU DONG CHUAN BI JDK 17 & CAI DAT CALOOUT LEN DIEN THOAI" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

$jdkDir = "e:\23.app caloout\CaloOut\jdk17"

if (-not (Test-Path "$jdkDir\bin\java.exe")) {
    $zipPath = "$env:TEMP\ms-jdk17.zip"
    Write-Host "[1/4] Dang tai OpenJDK 17 tu Microsoft CDN..." -ForegroundColor Yellow
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $wc = New-Object System.Net.WebClient
    $wc.DownloadFile("https://aka.ms/download-jdk/microsoft-jdk-17.0.12-windows-x64.zip", $zipPath)
    
    Write-Host "[2/4] Dang giai nen JDK 17..." -ForegroundColor Yellow
    $tempExtract = "$env:TEMP\jdk17_temp"
    if (Test-Path $tempExtract) { Remove-Item $tempExtract -Recurse -Force }
    Expand-Archive -Path $zipPath -DestinationPath $tempExtract -Force
    
    $extractedFolder = Get-ChildItem -Path $tempExtract | Where-Object { $_.PSIsContainer } | Select-Object -First 1
    if (Test-Path $jdkDir) { Remove-Item $jdkDir -Recurse -Force }
    Move-Item -Path $extractedFolder.FullName -Destination $jdkDir -Force
    
    Remove-Item $zipPath -Force -ErrorAction SilentlyContinue
    Remove-Item $tempExtract -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "[+] Da cai xong JDK 17" -ForegroundColor Green
} else {
    Write-Host "[+] JDK 17 da co san" -ForegroundColor Green
}

# Thiet lap JAVA_HOME tro vao JDK 17
$env:JAVA_HOME = $jdkDir
$env:PATH = "$jdkDir\bin;$env:PATH"

Write-Host ""
Write-Host "[3/4] Dang bien dich va cai dat APK len dien thoai..." -ForegroundColor Yellow
& .\gradlew.bat installDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "==========================================================" -ForegroundColor Green
    Write-Host "   THANH CONG: DA CAI DAT CALOOUT LEN DIEN THOAI!" -ForegroundColor Green
    Write-Host "==========================================================" -ForegroundColor Green
    
    $builtApk = "app\build\outputs\apk\debug\app-debug.apk"
    if (Test-Path $builtApk) {
        Copy-Item -Path $builtApk -Destination "e:\23.app caloout\CaloOut\CaloOut.apk" -Force
        Write-Host "Da xuat file APK: e:\23.app caloout\CaloOut\CaloOut.apk" -ForegroundColor Cyan
    }
    
    $adb = "C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools\adb.exe"
    if (Test-Path $adb) {
        Write-Host ""
        Write-Host "[4/4] Dang bat ung dung tren man hinh dien thoai..." -ForegroundColor Green
        & $adb shell am start -n com.caloout.calculator/.MainActivity
    }
    Write-Host ""
    Write-Host "APP DANG CHAY TREN DIEN THOAI CUA BAN!" -ForegroundColor Green
} else {
    Write-Host "Co loi trong qua trinh build." -ForegroundColor Red
}
