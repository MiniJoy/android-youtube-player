#!/usr/bin/env bash

./gradlew clean assembleRelease --offline
if [ $? -eq 0 ];then
    ./gradlew artifactoryPublish
fi