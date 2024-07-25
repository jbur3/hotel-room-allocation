#!/usr/bin/env sh

JAR_PATH=../build/libs/
GRADLE_WRAPPER_PATH=../
JAR_NAME=HotelRoomAllocation.jar

if [ ! -f "$JAR_PATH/$JAR_NAME" ]; then
  echo "JAR does not exist. Building project..."
  cd $GRADLE_WRAPPER_PATH
  ./gradlew clean build
  JAR_PATH=build/libs/
fi

echo "Starting HotelRoomAllocation..."
cd $JAR_PATH
java -jar $JAR_NAME
