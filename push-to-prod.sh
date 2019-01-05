#!/usr/bin/env bash

cf target -s prod

cd backend
./gradlew clean build

cd ../frontend
rm -rf node_modules && npm install && npm run build:prod && rm -rf node_modules/.cache
cd ..

cf push -f manifests/production/backend.yml && cf push -f manifests/production/frontend.yml