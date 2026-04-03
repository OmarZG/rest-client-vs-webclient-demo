@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0")

@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@IF NOT "%MAVEN_BASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF "%MAVEN_PROJECTBASEDIR:~-1%"=="\" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    @IF "%%A"=="distributionUrl" SET DISTRIBUTION_URL=%%B
    @IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@REM --- CONFIGURACION FORZADA DE JAVA 17 ---
@SET MAVEN_JAVA_EXE="C:\Java\jdk-17.0.0.1\bin\java.exe"
@REM -----------------------------------------

@SET WRAPPER_JAR_MISSING=FALSE
@IF NOT EXIST %WRAPPER_JAR% (
    SET WRAPPER_JAR_MISSING=TRUE
)

@IF "%WRAPPER_JAR_MISSING%"=="TRUE" (
    @ECHO Downloading Maven Wrapper...
    @curl.exe -fsSL -o %WRAPPER_JAR% %DOWNLOAD_URL%
)

@REM Ejecución corregida: Se eliminó la ruta absoluta del .properties que causaba el error 'C'
%MAVEN_JAVA_EXE% -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" ^
    -Dmaven.home="" ^
    -classpath %WRAPPER_JAR% ^
    "-Dmaven.repo.local=%USERPROFILE%\.m2\repository" ^
    %WRAPPER_LAUNCHER% %*
