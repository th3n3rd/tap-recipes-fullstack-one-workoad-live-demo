#!/bin/bash

APP_URL=${1:-$(kubectl get kservice fullstack -o yaml | yq '.status.url' | tr -d '\n' )}

pushd e2e
    if ! APP_URL="$APP_URL" npm run test ; then
        echo "Smoke test failed"
        exit 1
    fi
popd

echo "Smoke test succeeded"
