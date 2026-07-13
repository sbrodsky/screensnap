# Quick Start: Rebuilding ScreenSnap

## One Command Fix

Just run this command in the project directory:

```bash
mvn clean package
```

Then run:

```bash
java -jar target/ScreenSnap.jar
```

---

## Step-by-Step for Windows

### Option 1: Using Batch Script (Easiest)

1. Open Command Prompt
2. Navigate to the project:
   ```cmd
   cd C:\screensnap
   ```
3. Run the rebuild script:
   ```cmd
   rebuild.bat
   ```
4. When it completes successfully, run:
   ```cmd
   java -jar target\ScreenSnap.jar
   ```

### Option 2: Using PowerShell

1. Open PowerShell
2. Navigate to the project:
   ```powershell
   cd C:\screensnap
   ```
3. If you get an execution policy error, run:
   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```
4. Run the rebuild script:
   ```powershell
   .\rebuild.ps1
   ```
5. When it completes successfully, run:
   ```powershell
   java -jar target\ScreenSnap.jar
   ```

### Option 3: Using Maven Directly

1. Open Command Prompt or PowerShell
2. Navigate to the project:
   ```cmd
   cd C:\screensnap
   ```
3. Build the project:
   ```cmd
   mvn clean package
   ```
4. Wait for "BUILD SUCCESS" message
5. Run the application:
   ```cmd
   java -jar target\ScreenSnap.jar
   ```

---

## What to Expect

### During Build:
- Maven will download dependencies (first time takes longer)
- Compilation happens
- JAR is created with all dependencies bundled
- Should see: `BUILD SUCCESS`

### When Running:
- No console window needed
- A system tray icon should appear (if supported on your OS)
- Press F8 anywhere to capture a screenshot
- A crosshair overlay appears
- Select area by dragging
- Screenshot saved to Desktop as `ScreenSnap_YYYYMMDD_HHmmss.png`

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Maven command not found | Install Maven from https://maven.apache.org/download.cgi |
| Build fails | Check internet connection, then try `mvn clean package -U` |
| ClassNotFoundException still occurs | Delete `target` folder and rebuild: `mvn clean package` |
| Permission denied on .ps1 scripts | Run PowerShell as Administrator first |
| F8 hotkey doesn't work | Some apps/games may capture F8 first. Try a different hotkey (requires code change). |

---

## Success Indicators

✅ You should see:
- `[INFO] BUILD SUCCESS`
- JAR file created at `target/ScreenSnap.jar` (~675 KB)
- Application starts without ClassNotFoundException
- System tray icon appears (Windows/Mac typically)
- F8 hotkey triggers screenshot mode

---

## Verification Commands

Check that the fix is correct:

```bash
# List classes in the JAR
jar tf target/ScreenSnap.jar | find "ScreenSnap"

# Check the manifest
jar xf target/ScreenSnap.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
```

You should see the main class as: `com.screensnap.ScreenSnap`

---

Good luck! 🎉

