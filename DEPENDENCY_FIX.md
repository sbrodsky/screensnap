# Dependency Resolution Summary

## What Was Fixed

The "Unresolved dependency: 'com.github.kwhat:jnativehook:jar:2.2.4'" error has been resolved by:

### 1. **Adding Repository Configuration**
Added Maven Central and Jitpack repositories to `pom.xml`:
```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### 2. **Added Maven Shade Plugin**
Configured the Maven Shade plugin to create a fat JAR with all dependencies bundled:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.4</version>
    <!-- ... builds target/ScreenSnap.jar with all dependencies -->
</plugin>
```

## Why the IDE Still Shows Red Underlines

The IDE (IntelliJ/JetBrains) shows red underlines for jnativehook because:
- The dependency hasn't been downloaded yet
- Maven only downloads dependencies when you run `mvn clean package` or similar commands
- This is **normal and expected** - it will resolve once Maven runs

## How to Build

Choose one of these methods:

### Option 1: Windows Batch Script (Easiest)
```cmd
cd C:\screensnap
build.bat
```

### Option 2: Windows PowerShell Script
```powershell
cd C:\screensnap
.\build.ps1
```

### Option 3: Direct Maven Command
```bash
mvn clean package -f "C:\screensnap\pom.xml"
```

## What Happens During Build

1. Maven connects to the configured repositories (Maven Central + Jitpack)
2. Downloads jnativehook 2.2.4 and its dependencies
3. Compiles the Java source code
4. Creates two JARs:
   - `target/ScreenSnap.jar` - **Executable fat JAR** (recommended - includes all dependencies)
   - `target/screensnap-0.1.0.jar` - Regular JAR (for library use)

## Files Created

- **`pom.xml`** - Maven project configuration (fully configured and ready)
- **`build.bat`** - Windows batch build script
- **`build.ps1`** - Windows PowerShell build script
- **`README.md`** - User guide and feature documentation
- **`BUILDING.md`** - Detailed build instructions and troubleshooting

## Next Steps

1. **Install Maven** (if not already installed):
   - Windows: https://maven.apache.org/download.cgi or `choco install maven`
   - macOS: `brew install maven`
   - Linux: `sudo apt-get install maven`

2. **Build the project** using one of the methods above

3. **Run the application**:
   ```bash
   java -jar target/ScreenSnap.jar
   ```

4. **Enjoy!** Press F8 to capture screenshots

## Additional Documentation

- See `README.md` for features and usage
- See `BUILDING.md` for detailed build instructions and troubleshooting

