#!/usr/bin/env bash

cd production-code/backend && ./gradlew clean build -x test &&

cd ../.. &&

mv production-code/backend build-output/backend &&
mv production-code/manifests build-output/manifests