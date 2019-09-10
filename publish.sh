#!/usr/bin/env bash

./gradlew clean build bintrayUpload -PbintrayUser=felix0503 -PbintrayKey=bc807e709bfc00153557f05185cf8fd0d1a18809 -PdryRun=false