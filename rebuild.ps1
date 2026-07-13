# Quick rebuild script for ScreenSnap after ClassNotFoundException fix

Write-Host "Cleaning old build..." -ForegroundColor Yellow
Remove-Item -Path "target" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "Building ScreenSnap with fixed package structure..." -ForegroundColor Cyan
Write-Host ""

Push-Location $PSScriptRoot

# Check if Maven is installed
$mavenPath = Get-Command mvn -ErrorAction SilentlyContinue

if (-not $mavenPath) {
    Write-Host "ERROR: Maven not found in PATH" -ForegroundColor Red
    Write-Host "Please ensure Maven is installed and in your PATH" -ForegroundColor Yellow
    Write-Host "See BUILDING.md for installation instructions" -ForegroundColor Yellow
    exit 1
}

mvn clean package

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "  Build Successful!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "You can now run ScreenSnap:" -ForegroundColor Green
    Write-Host "  java -jar target\ScreenSnap.jar" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "Build failed. See error messages above." -ForegroundColor Red
    exit 1
}

Pop-Location

