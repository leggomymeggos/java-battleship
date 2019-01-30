#!/usr/bin/env bash

cd source-code/backend && ./gradlew clean build &&
cd ../frontend && npm test;