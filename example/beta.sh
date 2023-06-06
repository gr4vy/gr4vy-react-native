#!/bin/sh
export $(grep -v '^#' .env.preview | xargs) && \
cd "$1" && bundle exec fastlane beta "$2" "$3" && cd ..