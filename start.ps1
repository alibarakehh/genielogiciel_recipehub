# Recipe Website Startup Script
# Windows PowerShell

Write-Host "=======================================" -ForegroundColor Cyan
Write-Host "  Recipe Website - Spring Boot App    " -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan
Write-Host ""

# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
Write-Host "✓ Java Home set to: $env:JAVA_HOME" -ForegroundColor Green

# Check if Maven wrapper exists
if (-Not (Test-Path ".\mvnw.cmd")) {
    Write-Host "✗ Maven wrapper not found!" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Maven wrapper found" -ForegroundColor Green
Write-Host ""

# Ask user what to do
Write-Host "Select an option:" -ForegroundColor Yellow
Write-Host "1. Build and Run (recommended for first time)"
Write-Host "2. Run only (if already built)"
Write-Host "3. Build only"
Write-Host "4. Run tests"
Write-Host "5. Clean and rebuild"
Write-Host ""

$choice = Read-Host "Enter your choice (1-5)"

switch ($choice) {
    "1" {
        Write-Host "`nBuilding and running the application..." -ForegroundColor Cyan
        .\mvnw.cmd clean package -DskipTests
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`n✓ Build successful!" -ForegroundColor Green
            Write-Host "Starting application..." -ForegroundColor Cyan
            Write-Host ""
            Write-Host "=======================================" -ForegroundColor Green
            Write-Host "  Application URLs:                  " -ForegroundColor Green
            Write-Host "  - API: http://localhost:8080       " -ForegroundColor Green
            Write-Host "  - Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Green
            Write-Host "  - H2 Console: http://localhost:8080/h2-console" -ForegroundColor Green
            Write-Host "=======================================" -ForegroundColor Green
            Write-Host ""
            java -jar target\recipe-website-1.0.0.jar
        } else {
            Write-Host "`n✗ Build failed!" -ForegroundColor Red
        }
    }
    "2" {
        if (Test-Path "target\recipe-website-1.0.0.jar") {
            Write-Host "`nStarting application..." -ForegroundColor Cyan
            Write-Host ""
            Write-Host "=======================================" -ForegroundColor Green
            Write-Host "  Application URLs:                  " -ForegroundColor Green
            Write-Host "  - API: http://localhost:8080       " -ForegroundColor Green
            Write-Host "  - Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Green
            Write-Host "  - H2 Console: http://localhost:8080/h2-console" -ForegroundColor Green
            Write-Host "=======================================" -ForegroundColor Green
            Write-Host ""
            java -jar target\recipe-website-1.0.0.jar
        } else {
            Write-Host "`n✗ JAR file not found! Please build first." -ForegroundColor Red
        }
    }
    "3" {
        Write-Host "`nBuilding the application..." -ForegroundColor Cyan
        .\mvnw.cmd clean package -DskipTests
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`n✓ Build successful!" -ForegroundColor Green
        } else {
            Write-Host "`n✗ Build failed!" -ForegroundColor Red
        }
    }
    "4" {
        Write-Host "`nRunning tests..." -ForegroundColor Cyan
        .\mvnw.cmd test
    }
    "5" {
        Write-Host "`nCleaning and rebuilding..." -ForegroundColor Cyan
        .\mvnw.cmd clean install
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`n✓ Clean and rebuild successful!" -ForegroundColor Green
        } else {
            Write-Host "`n✗ Build failed!" -ForegroundColor Red
        }
    }
    default {
        Write-Host "`n✗ Invalid choice!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
