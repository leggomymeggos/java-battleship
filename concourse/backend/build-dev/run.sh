#!/usr/bin/env bash

cd source-code/backend && ./gradlew clean build -x test &&

cd ../.. &&

mv source-code/backend build-output/backend &&
mv source-code/manifests build-output/manifests