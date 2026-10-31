@echo off
set "MAVEN_EXEC=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.3.4.1\plugins\maven\lib\maven3\bin\mvn.cmd"
if exist "%MAVEN_EXEC%" (
    call "%MAVEN_EXEC%" %*
) else (
    mvn %*
)
