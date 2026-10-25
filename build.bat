@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
if not exist gen mkdir gen

java -jar lib\jflex-full-1.9.1.jar -d gen src\lexer\parenteses.flex
if errorlevel 1 exit /b 1

javac -encoding UTF-8 -d bin gen\Lexer.java src\Token.java src\AutomatoPilha.java src\Main.java
if errorlevel 1 exit /b 1

if "%~1"=="" (
  echo ===== validas =====
  java -cp bin Main tests\validas.txt
  echo.
  echo ===== invalidas =====
  java -cp bin Main tests\invalidas.txt
) else (
  java -cp bin Main "%~1"
)
