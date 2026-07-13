# ScreenSnap

A lightweight screenshot utility with global hotkey support (CTRL-SHIFT-N) and system tray integration.

## Project Structure

```
screensnap/
├── src/main/java/
│   └── ScreenSnap.java         # Main application (com.screensnap package)
├── pom.xml                      # Maven configuration
├── build.bat                    # Windows batch build script
├── build.ps1                    # Windows PowerShell build script
└── target/
    └── ScreenSnap.jar           # Output: executable fat JAR with all dependencies
```

## Features

- Press **CTRL-SHIFT-N** globally to trigger screenshot capture
- Select area with crosshair overlay (3:4 aspect ratio)
- Auto-saves to Desktop with timestamp
- System tray integration with notifications
- Cross-platform (Windows, macOS, Linux)

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Building

### Option 1: Build a fat JAR (recommended - includes all dependencies)

```bash
mvn clean package
```

This creates `target/ScreenSnap.jar` which includes all dependencies and can be run on any machine with Java 11+.

### Option 2: Build without dependencies

```bash
mvn clean compile
```

## Running

### Run the fat JAR:

```bash
java -jar target/ScreenSnap.jar
```

### Or run directly with Maven:

```bash
mvn compile exec:java -Dexec.mainClass="ScreenSnap"
```

## Dependencies

- **jnativehook** (2.2.4) - Global keyboard hook for CTRL-SHIFT-N hotkey detection
- Java AWT/Swing - For GUI and screenshot capture

## How to Use

1. Start the application - a tray icon should appear
2. Press **CTRL-SHIFT-N** anywhere on your screen
3. A crosshair overlay will appear - drag to select the area to capture
4. Release mouse to save the screenshot
5. Right-click to cancel the selection, or press **ESC**
6. Screenshot is saved to `~/Desktop/ScreenSnap_YYYYMMDD_HHmmss.png`

## Troubleshooting

### "Cannot resolve symbol 'github'"
This means Maven dependencies haven't been downloaded yet. Run:
```bash
mvn clean package
```

### CTRL-SHIFT-N hotkey not working
- On Linux, you may need appropriate permissions for global keyboard hooks
- Ensure jnativehook is properly installed by checking the build output
- Some desktop environments or applications may intercept the hotkey
- If you need a different hotkey, edit `src/main/java/ScreenSnap.java`, find the `nativeKeyListener` section, and modify the key checks

### Tray icon not showing
- System tray may not be supported on your OS (e.g., some Linux desktop environments)
- The application will still work - just use the CTRL-SHIFT-N hotkey

## Architecture

- `ScreenSnap.java` - Main application class with global hotkey listener
- `SelectionOverlay` (inner class) - Handles screenshot selection UI and capture

## License

MIT License

