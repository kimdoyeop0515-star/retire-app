#!/bin/sh
##
## Gradle wrapper script - auto-downloads wrapper jar if missing
##

set -e

SCRIPT_DIR=$(dirname "$0")
WRAPPER_JAR="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_PROPS="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.properties"

# wrapper jar가 없으면 다운로드
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Gradle wrapper jar..."
    WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v8.6.0/gradle/wrapper/gradle-wrapper.jar"
    if command -v curl >/dev/null 2>&1; then
        curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" 2>/dev/null || true
    elif command -v wget >/dev/null 2>&1; then
        wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR" 2>/dev/null || true
    fi
fi

# Java 확인
if [ -z "$JAVA_HOME" ]; then
    if command -v java >/dev/null 2>&1; then
        JAVA_CMD="java"
    else
        echo "Error: JAVA_HOME not set and java not found"
        exit 1
    fi
else
    JAVA_CMD="$JAVA_HOME/bin/java"
fi

exec "$JAVA_CMD" -jar "$WRAPPER_JAR" "$@"
