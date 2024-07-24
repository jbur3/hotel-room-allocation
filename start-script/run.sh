#!/usr/bin/env sh

JAR_PATH=build/libs/
GRADLE_WRAPPER_PATH=../

cd $GRADLE_WRAPPER_PATH
./gradlew clean build
cd $JAR_PATH
java -jar HotelRoomAllocation-0.0.1-SNAPSHOT.jar