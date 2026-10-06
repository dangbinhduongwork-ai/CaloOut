# PowerShell script to push to GitHub
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "         TIEN TRINH PUSH CODE LEN GITHUB" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

# 1. Config git author if missing
$gitUser = git config user.name
if (-not $gitUser) {
    git config user.name "dangbinhduongwork-ai"
    git config user.email "dangbinhduongwork-ai@users.noreply.github.com"
}

# 2. Git init & add & commit
git init
git add .
git commit -m "feat: initial CaloOut Android app"

# 3. Branch & remote
git branch -M main
git remote remove origin 2>$null
git remote add origin https://github.com/dangbinhduongwork-ai/CaloOut.git

# 4. Push
Write-Host "Dang push len GitHub..." -ForegroundColor Yellow
git push -u origin main

if ($LASTEXITCODE -ne 0) {
    Write-Host "Dang thu lai voi flag --force..." -ForegroundColor Yellow
    git push -u origin main --force
}

Write-Host "Hoan tat!" -ForegroundColor Green
