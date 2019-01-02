#!/usr/bin/env bash

pushd ~/workspace/java-battleship;

cd backend && ./gradlew clean build &&
cd ../frontend && npm test;

if [[ $? == 0 ]]; then
    popd && gosleap
else
    popd && ./ganonleap.sh -e 2
fi
