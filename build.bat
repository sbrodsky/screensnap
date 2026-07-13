@echo off
REM ScreenSnap Build Script for Windows
REM This script builds the ScreenSnap screenshot utility

setlocal enabledelayedexpansion

echo.
echo ============================================
echo  ScreenSnap Build Script
echo ============================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo.
    echo Please install Maven from: https://maven.apache.org/download.cgi
    echo And add the Maven bin directory to your PATH environment variable
    echo.
    echo Common paths:
    echo   - Windows: C:\Program Files\Maven\bin
    echo   - Or use Chocolatey: choco install maven
    echo.
    exit /b 1
)

echo Maven found:
mvn --version
echo.

REM Build the project
echo Building ScreenSnap...
echo.

mvn clean package -f "%~dp0pom.xml"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo  Build Successful!
    echo ============================================
    echo.
    echo JAR file created at:
    echo   %~dp0target\ScreenSnap.jar
    echo.
    echo To run the application:
    echo   java -jar "%~dp0target\ScreenSnap.jar"
    echo.
) else (
    echo.
    echo ============================================
    echo  Build Failed!
    echo ============================================
    echo.
    echo See error messages above for details.
    exit /b 1
)

endlocal

