# CaloOut - Script cai dat va khoi chay truc tiep len dien thoai Android qua USB
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "    DANG CAI DAT VA KHOI CHAY CALOOUT LEN DIEN THOAI" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

Set-Location -Path $PSScriptRoot

$adbPath = "C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# 1. Kiem tra thiet bi ket noi qua ADB
if (Test-Path $adbPath) {
    Write-Host "[*] Dang kiem tra ket noi thiet bi qua ADB..." -ForegroundColor Yellow
    $devices = & $adbPath devices
    $devices | Out-String | Write-Host
}

# 2. Dam bao gradle-wrapper.jar da co san
$wrapperJar = "gradle\wrapper\gradle-wrapper.jar"
if (-not (Test-Path $wrapperJar)) {
    Write-Host "[*] Dang chuan bi Gradle Wrapper..." -ForegroundColor Yellow
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri "https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar" -OutFile $wrapperJar
}

# 3. Bien dich va cai dat truc tiep len dien thoai
Write-Host ""
Write-Host "[*] Dang bien dich va cai dat ung dung len dien thoai (installDebug)..." -ForegroundColor Yellow
Write-Host "[*] Vui long mo khoa man hinh dien thoai..." -ForegroundColor Yellow
Write-Host ""

& .\gradlew.bat installDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host "       [THANH CONG] DA CAI DAT XONG LEN DIEN THOAI!" -ForegroundColor Green
    Write-Host "========================================================" -ForegroundColor Green
    
    # 4. Tu dong mo app tren dien thoai
    if (Test-Path $adbPath) {
        Write-Host ""
        Write-Host "[*] Dang tu dong bat ung dung tren man hinh dien thoai..." -ForegroundColor Green
        & $adbPath shell am start -n com.caloout.calculator/.MainActivity
    }
    Write-Host "[+] Hoan tat! Hay kiem tra man hinh dien thoai cua ban." -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Red
    Write-Host "               LUU Y KHI CAI DAT QUA USB" -ForegroundColor Red
    Write-Host "========================================================" -ForegroundColor Red
    Write-Host "1. Vao Cai dat tren dien thoai -> Tuy chon nha phat trien (Developer Options)." -ForegroundColor Yellow
    Write-Host "2. Bat muc: 'Go loi USB' (USB Debugging)." -ForegroundColor Yellow
    Write-Host "3. Khi dien thoai hien thong bao 'Cho phep go loi USB tu may tinh nay?', chon 'Cho phep' (Allow)." -ForegroundColor Yellow
    Write-Host "4. Sau do chay lai lenh nay." -ForegroundColor Yellow
}
