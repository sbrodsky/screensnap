# ClassNotFoundException Fix

## Problem

When running the previously built JAR:
```
java -jar target/ScreenSnap.jar
Error: Could not find or load main class ScreenSnap
Caused by: java.lang.ClassNotFoundException: ScreenSnap
```

## Root Cause

The original `ScreenSnap.java` was in the project root directory instead of the Maven standard source directory structure (`src/main/java`). Additionally, it was in the default package (no package declaration), which prevented Maven from properly packaging it into the JAR.

## Solution Applied

1. **Created Maven source structure**: `src/main/java/`
2. **Moved ScreenSnap.java** to: `src/main/java/ScreenSnap.java`
3. **Added package declaration**: `package com.screensnap;`
4. **Updated pom.xml**: Changed main class references from `ScreenSnap` to `com.screensnap.ScreenSnap` in both:
   - maven-jar-plugin configuration
   - maven-shade-plugin configuration

## Files Changed

- **`ScreenSnap.java`** - Moved to `src/main/java/` and added package declaration
- **`pom.xml`** - Updated main class names to include package
- Old `ScreenSnap.java` in root - Can be deleted (new version is in src/main/java/)

## How to Rebuild

### Windows (Batch):
```cmd
cd C:\screensnap
build.bat
```

### Windows (PowerShell):
```powershell
cd C:\screensnap
.\build.ps1
```

### Any OS (Direct Maven):
```bash
cd C:\screensnap
mvn clean package
```

## Running After Rebuild

```bash
java -jar target/ScreenSnap.jar
```

The application should now start successfully with the global CTRL-ALT-K hotkey listener and system tray integration.

## Project Structure

```
screensnap/
├── src/
│   └── main/
│       └── java/
│           └── ScreenSnap.java          ← Source code (new location)
├── target/
│   ├── ScreenSnap.jar                   ← Executable JAR (regenerated)
│   ├── classes/
│   │   └── com/
│   │       └── screensnap/
│   │           └── ScreenSnap.class     ← Compiled class
│   └── ...
├── pom.xml                              ← Maven configuration (updated)
├── build.bat                            ← Build script
└── build.ps1                            ← Build script
```

## Verification

After rebuilding, verify the JAR contains the class correctly:

```bash
jar tf target/ScreenSnap.jar | grep -i screensnap
# Should output: com/screensnap/ScreenSnap.class
```

Or view the manifest:
```bash
jar xf target/ScreenSnap.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
# Should show: Main-Class: com.screensnap.ScreenSnap
```

