# ScreenSnap Build Script for PowerShell
# This script builds the ScreenSnap screenshot utility

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  ScreenSnap Build Script (PowerShell)" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mavenPath = (Get-Command mvn -ErrorAction SilentlyContinue).Source

if (-not $mavenPath) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "And add the Maven bin directory to your PATH environment variable" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Common paths:" -ForegroundColor Yellow
    Write-Host "  - Windows: C:\Program Files\Maven\bin" -ForegroundColor Yellow
    Write-Host "  - Or use Chocolatey: choco install maven" -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

Write-Host "Maven found: $mavenPath" -ForegroundColor Green
mvn --version
Write-Host ""

Write-Host "Building ScreenSnap..." -ForegroundColor Cyan
Write-Host ""

$scriptDir = Split-Path -Parent $PSCommandPath
mvn clean package -f "$scriptDir\pom.xml"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "  Build Successful!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "JAR file created at:" -ForegroundColor Green
    Write-Host "  $scriptDir\target\ScreenSnap.jar" -ForegroundColor Green
    Write-Host ""
    Write-Host "To run the application:" -ForegroundColor Yellow
    Write-Host "  java -jar `"$scriptDir\target\ScreenSnap.jar`"" -ForegroundColor Yellow
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Red
    Write-Host "  Build Failed!" -ForegroundColor Red
    Write-Host "============================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "See error messages above for details." -ForegroundColor Red
    exit 1
}

