# Building ScreenSnap

This document provides detailed instructions for building ScreenSnap from source.

## System Requirements

- **Java Development Kit (JDK)** 11 or higher
  - Download: https://adoptopenjdk.net/ or https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version` and `javac -version`
  
- **Maven** 3.6.0 or higher
  - Download: https://maven.apache.org/download.cgi
  - Verify: `mvn --version`

## Installation

### Maven Installation

**Windows:**

1. Download Apache Maven from https://maven.apache.org/download.cgi
2. Extract to a folder (e.g., `C:\Program Files\Maven`)
3. Add Maven `bin` directory to your PATH:
   - Open System Properties → Environment Variables
   - Add `C:\Program Files\Maven\bin` to PATH
4. Restart your terminal and verify: `mvn --version`

Alternatively, use Chocolatey:
```powershell
choco install maven
```

**macOS:**

```bash
brew install maven
```

**Linux (Ubuntu/Debian):**

```bash
sudo apt-get install maven
```

## Building the Project

### Quick Build (Recommended)

**Windows (Command Prompt):**
```cmd
cd C:\screensnap
build.bat
```

**Windows (PowerShell):**
```powershell
cd C:\screensnap
.\build.ps1
```

**macOS/Linux (Bash):**
```bash
cd ~/screensnap
mvn clean package
```

### Manual Build

```bash
cd <screensnap-directory>
mvn clean package
```

This will:
1. Download all dependencies (jnativehook and its transitive dependencies)
2. Compile the Java source code
3. Run any tests
4. Package the application into a fat JAR with all dependencies included
5. Create `target/ScreenSnap.jar`

## Output

After a successful build, you will find:

- **`target/ScreenSnap.jar`** - Executable JAR with all dependencies included (can be run on any machine with Java 11+)
- **`target/classes/`** - Compiled class files
- **`target/original-screensnap-0.1.0.jar`** - Regular JAR without dependencies (for library use)

## Running the Application

### From the Built JAR

```bash
java -jar target/ScreenSnap.jar
```

### Directly with Maven

```bash
mvn compile exec:java -Dexec.mainClass="ScreenSnap"
```

(Note: This requires all dependencies to be downloaded first)

## Troubleshooting

### "Cannot resolve symbol 'github'" or "Dependency not found"

These are IDE warnings that appear before Maven downloads the dependencies. They will disappear after running:

```bash
mvn clean package
```

The Maven build will fetch all dependencies from the configured repositories.

### Maven command not found

**Solution:** Maven is not installed or not in your PATH. See Installation section above.

### Java version too old

Make sure you have JDK 11 or higher:

```bash
java -version
```

If you have an older version, download and install a newer JDK.

### Build fails with network errors

If Maven cannot download dependencies:

1. Check your internet connection
2. Try forcing an update: `mvn clean package -U`
3. Clear Maven cache and try again:
   ```bash
   rm -rf ~/.m2/repository  # Linux/macOS
   rmdir %USERPROFILE%\.m2\repository  # Windows
   mvn clean package
   ```

### "Dependency 'com.github.kwhat:jnativehook:2.2.4' not found"

This error should resolve once Maven connects to the repositories. If it persists:

1. Verify your internet connection
2. Check Maven settings: `mvn help:describe -Dplugin=help`
3. The pom.xml is configured to use both Maven Central and Jitpack repositories
4. Try forcing a clean rebuild: `mvn clean -U package`

## Customizing the Build

### Change Java Version

Edit `pom.xml` and change the `maven.compiler.source` and `maven.compiler.target` properties (default is 11):

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>
```

### Build Without Shading

If you want a lightweight JAR without bundled dependencies:

```bash
mvn clean jar:jar
```

This creates `target/screensnap-0.1.0.jar` without the Shade plugin processing.

### Enable Verbose Logging

For detailed build information:

```bash
mvn clean package -X
```

Or with less verbosity:

```bash
mvn clean package -q
```

## Continuous Building

Watch for file changes and rebuild automatically:

```bash
mvn clean compile watch
```

Or using the Takari lifecycle extension (if installed):

```bash
mvn -f pom.xml clean compile -Dtakari.lifecycleMappingVersion=1.7.0
```

## Next Steps

See `README.md` for usage instructions and features.

