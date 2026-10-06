@echo off
chcp 65001 >nul
title CaloOut - Push to GitHub
cd /d "%~dp0"

echo ========================================================
echo         ĐANG TIẾN HÀNH PUSH CODE LÊN GITHUB
echo ========================================================
echo.

:: 1. Kiểm tra cấu hình tác giả Git
git config user.name >nul 2>&1
if errorlevel 1 (
    echo [*] Thiết lập tên tác giả Git...
    git config user.name "dangbinhduongwork-ai"
    git config user.email "dangbinhduongwork-ai@users.noreply.github.com"
)

:: 2. Khởi tạo Git nếu chưa có
echo [*] Khởi tạo git repository...
git init

:: 3. Thêm toàn bộ tệp tin
echo [*] Thêm các tệp tin vào git...
git add .

:: 4. Commit
echo [*] Tạo commit ban đầu...
git commit -m "feat: initial CaloOut TDEE & BMR Jetpack Compose Android app"

:: 5. Chuyển nhánh sang main
echo [*] Đặt tên nhánh là main...
git branch -M main

:: 6. Thiết lập Remote
echo [*] Kết nối tới GitHub: https://github.com/dangbinhduongwork-ai/CaloOut.git
git remote remove origin 2>nul
git remote add origin https://github.com/dangbinhduongwork-ai/CaloOut.git

:: 7. Push lên GitHub
echo.
echo [*] Đang push lên GitHub...
git push -u origin main

if errorlevel 1 (
    echo.
    echo [*] Thử push với cờ --force...
    git push -u origin main --force
)

echo.
echo ========================================================
echo                  HOÀN TẤT TIẾN TRÌNH!
echo ========================================================
echo Kiểm tra tiến trình build APK tại:
echo https://github.com/dangbinhduongwork-ai/CaloOut/actions
echo.
pause
