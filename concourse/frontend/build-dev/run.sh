#!/usr/bin/env bash

cd source-code/frontend && npm install && npm run build:dev &&

cd ../.. &&

mv source-code/frontend build-output/frontend &&
mv source-code/manifests build-output/manifests