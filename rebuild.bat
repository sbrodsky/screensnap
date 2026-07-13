@echo off
REM Quick rebuild script for ScreenSnap after ClassNotFoundException fix

echo Cleaning old build...
rmdir /s /q target 2>nul

echo.
echo Building ScreenSnap with fixed package structure...
echo.

cd /d "%~dp0"

REM Try to find Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven not found in PATH
    echo Please ensure Maven is installed and in your PATH
    echo See BUILDING.md for installation instructions
    exit /b 1
)

mvn clean package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo  Build Successful!
    echo ============================================
    echo.
    echo You can now run ScreenSnap:
    echo   java -jar target\ScreenSnap.jar
    echo.
) else (
    echo.
    echo Build failed. See error messages above.
    exit /b 1
)

