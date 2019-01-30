#!/usr/bin/env bash

cd source-code/backend && ./gradlew clean build -x test

mv source-code/backend build-output/backend