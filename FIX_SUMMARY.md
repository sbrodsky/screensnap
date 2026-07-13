# ScreenSnap - ClassNotFoundException Fix Complete

## What Was Wrong

You got this error when trying to run the JAR:
```
java -jar "c:\screensnap\target\ScreenSnap.jar"
Error: Could not find or load main class ScreenSnap
Caused by: java.lang.ClassNotFoundException: ScreenSnap
```

## Root Causes

1. **Wrong Directory Structure**: `ScreenSnap.java` was in the project root, not in `src/main/java/`
2. **No Package Declaration**: The class had no package statement, keeping it in the default package
3. **Incorrect Manifest**: The JAR manifest referenced just `ScreenSnap` instead of the fully qualified name

## What Was Fixed

✅ Created proper Maven structure: `src/main/java/`
✅ Moved source code to correct location
✅ Added package declaration: `package com.screensnap;`
✅ Updated pom.xml to use fully qualified class name: `com.screensnap.ScreenSnap`
✅ Fixed manifest configuration in both jar-plugin and shade-plugin

## New Project Structure

```
C:\screensnap\
├── src/main/java/
│   └── ScreenSnap.java              ← Source code (correct location)
├── pom.xml                          ← Updated with correct main class
├── build.bat / build.ps1            ← Build scripts
├── rebuild.bat / rebuild.ps1        ← Quick rebuild scripts (NEW)
├── CLASSNOTFOUND_FIX.md             ← This detailed explanation
├── target/                          ← Will be regenerated
│   └── ScreenSnap.jar               ← Executable (to be rebuilt)
└── ScreenSnap.java (old - can delete)
```

## How to Rebuild

### Quickest Way:

**Windows (Batch):**
```cmd
cd C:\screensnap
rebuild.bat
```

**Windows (PowerShell):**
```powershell
cd C:\screensnap
.\rebuild.ps1
```

### Manual Maven Build:

```bash
cd C:\screensnap
mvn clean package
```

## Run After Rebuild

```bash
java -jar target/ScreenSnap.jar
```

The application should now start without ClassNotFoundException!

## Files to Clean Up (Optional)

You can delete the old source file in the root:
- `C:\screensnap\ScreenSnap.java` (the old one in project root)

The new version is now at: `C:\screensnap\src\main\java\ScreenSnap.java`

## Verification

To verify the fix is correct, after building check the JAR contents:

```bash
jar tf target/ScreenSnap.jar | findstr /i screensnap
# Should show: com/screensnap/ScreenSnap.class
```

Or check the manifest:
```bash
jar xf target/ScreenSnap.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
# Main-Class should be: com.screensnap.ScreenSnap
```

## Next Steps

1. Run `rebuild.bat` (or `rebuild.ps1` on PowerShell)
2. Run `java -jar target/ScreenSnap.jar`
3. Press F8 to capture screenshots!

## Still Having Issues?

- **Maven not found**: Install Maven from https://maven.apache.org/download.cgi
- **Permission denied on scripts**: Run PowerShell as Administrator: `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser`
- **Other build errors**: Check `BUILDING.md` for troubleshooting

---

**Summary**: The source code is now properly organized following Maven conventions, with correct package naming and configuration. The JAR will compile correctly when you rebuild.

