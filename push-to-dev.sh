#!/usr/bin/env bash

cf target -s development

cd backend
./gradlew clean build

cd ../frontend
rm -rf node_modules && npm install && npm run build:dev && rm -rf node_modules/.cache
cd ..

cf push -f manifests/development/backend.yml && cf push -f manifests/development/frontend.yml