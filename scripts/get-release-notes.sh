#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Version name is required."
  echo "Usage: $0 <VersionName>"
  exit 1
fi

VERSION_NAME="$1"
VERSION_NAME="${VERSION_NAME#v}"

CHANGELOG_PATH="./CHANGELOG.md"
if [ ! -f "$CHANGELOG_PATH" ]; then
  echo "Error: Changelog file '$CHANGELOG_PATH' not found."
  exit 1
fi

CHANGELOG_CONTENT=$(cat "$CHANGELOG_PATH")

PATTERN="## \[$VERSION_NAME.*?\](.*?)(?=## \[|\$)"

RELEASE_NOTES=$(echo "$CHANGELOG_CONTENT" | \
  perl -0777 -ne "if (/($PATTERN)/s) { print \$1; exit 0 }")

if [ -n "$RELEASE_NOTES" ]; then
  echo "$RELEASE_NOTES" | sed 's/\r//g'
else
  echo "Error: Release notes for version '$VERSION_NAME' not found."
  exit 1
fi

exit 0
