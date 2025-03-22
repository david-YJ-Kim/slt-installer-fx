@echo off
rem 설치 프로그램 실행 스크립트
set JAVA_HOME=C:\Users\tspsc\.jdks\corretto-1.8.0_412
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=.;.\lib\*

rem JNA 디버깅 옵션 활성화
set JNA_OPTIONS=-Djna.nosys=true -Djna.nounpack=true -Djna.debug_load=true -Djna.library.path=.\lib

rem 네이티브 라이브러리 경로 지정
set JAVA_OPTS=-Djava.library.path=.\lib %JNA_OPTIONS%

echo 설치 프로그램 시작 중...
"%JAVA_HOME%\bin\java" %JAVA_OPTS% -jar ./target/installer-app-1.0.0.jar

rem 종료 코드 확인
if %errorlevel% neq 0 (
  echo 오류 발생: 종료 코드 %errorlevel%
  pause
)