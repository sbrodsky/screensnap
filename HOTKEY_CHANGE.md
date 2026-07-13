# Hotkey Change: F8 → CTRL-SHIFT-N

## What Changed

The screenshot hotkey has been changed from **F8** to **CTRL-SHIFT-N** to prevent accidental triggering.

### Why This Change?

- **F8** can be easily triggered accidentally, especially in gaming or while using other applications
- **CTRL-SHIFT-N** is a deliberate three-key combination that's much less likely to be pressed accidentally
- The new combination requires conscious intent to trigger a screenshot

## Code Changes

### Modified Files

1. **`src/main/java/ScreenSnap.java`**
   - Added modifier key tracking:
     ```java
     private static boolean ctrlPressed = false;
     private static boolean shiftPressed = false;
     ```
   - Updated key listener to detect CTRL-SHIFT-N combination
   - Updated tray icon tooltip from "F8 to capture" to "CTRL-SHIFT-N to capture"

2. **`README.md`**
   - Updated all references from F8 to CTRL-SHIFT-N
   - Updated usage instructions
   - Updated troubleshooting section

## How It Works

The new hotkey uses a three-key combination:

1. When **CTRL** is pressed → `ctrlPressed = true`
2. When **SHIFT** is pressed → `shiftPressed = true`
3. When **N** is pressed AND both `ctrlPressed` and `shiftPressed` are true → trigger screenshot
4. When **CTRL** or **SHIFT** is released → set corresponding flag to false

This ensures the hotkey only triggers when all three keys are intentionally pressed together.

## Rebuilding

### Quick Rebuild (Windows)

**Batch:**
```cmd
cd C:\screensnap
rebuild.bat
```

**PowerShell:**
```powershell
cd C:\screensnap
.\rebuild.ps1
```

### Manual Build

```bash
cd C:\screensnap
mvn clean package
```

## Running the Updated Application

```bash
java -jar target/ScreenSnap.jar
```

Then press **CTRL-SHIFT-N** to capture a screenshot.

## Customizing the Hotkey

If you want to use a different hotkey:

1. Edit `src/main/java/ScreenSnap.java`
2. Find the `nativeKeyListener` section (around line 67)
3. Modify the key codes:
   - Replace `NativeKeyEvent.VC_CONTROL` with a different modifier (e.g., `NativeKeyEvent.VC_ALT`)
   - Replace `NativeKeyEvent.VC_SHIFT` with another modifier or remove it
   - Replace `NativeKeyEvent.VC_N` with a different letter key (e.g., `VC_S` for S key)
4. Rebuild with `mvn clean package`

### Common Key Code Examples

- `VC_CONTROL` - CTRL key
- `VC_SHIFT` - SHIFT key
- `VC_ALT` - ALT key
- `VC_A` to `VC_Z` - Letter keys A-Z
- `VC_F1` to `VC_F12` - Function keys

Example: For **ALT-SHIFT-S** combination:
```java
if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
    altPressed = true;
} else if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
    shiftPressed = true;
} else if (e.getKeyCode() == NativeKeyEvent.VC_S && altPressed && shiftPressed) {
    showSelectionOverlay();
}
```

## Verification

After rebuilding, verify the hotkey works:

1. Start the application: `java -jar target/ScreenSnap.jar`
2. Press **CTRL-SHIFT-N** anywhere
3. The crosshair selection overlay should appear
4. Select area and take a screenshot

## Notes

- The hotkey is global - it works even when ScreenSnap window is not focused
- Make sure no other application uses the same hotkey combination
- On some Linux desktop environments, global hotkeys may require special permissions
- On macOS, you may see permission prompts when using global hotkeys - this is normal

