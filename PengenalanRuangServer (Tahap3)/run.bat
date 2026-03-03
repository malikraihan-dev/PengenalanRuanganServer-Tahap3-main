@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call mvnw.cmd spring-boot:run
pause
