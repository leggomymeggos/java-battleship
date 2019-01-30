#!/usr/bin/env bash

cd production-code/frontend && npm install && npm run build:prod &&

cd ../.. &&

mv production-code/frontend build-output/frontend &&
mv production-code/manifests build-output/manifests