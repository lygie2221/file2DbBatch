#!/bin/bash

INPUT_FILE="$1"
OUTPUT_FILE=""

if [[ -z "$INPUT_FILE" ]]; then
  echo "Usage: $0 <copybook-file>"
  exit 1
fi

to_camel_case() {
  local input="$1"
  echo "$input" | awk -F'-' '{
    result = $1
    for (i=2; i<=NF; i++) {
      sub(".", toupper(substr($i,1,1)), $i)
      result = result $i
    }
    print result
  }'
}

CLASS_NAME=""
JAVA_FIELDS=""

while IFS= read -r line; do
  line=$(echo "$line" | sed 's/^[ \t]*//;s/[ \t]*$//')

  if [[ "$line" =~ ^01[[:space:]]+([a-zA-Z0-9_-]+)\. ]]; then
    RAW_CLASS="${BASH_REMATCH[1]}"
    CLASS_NAME=$(to_camel_case "$RAW_CLASS")
    OUTPUT_FILE="${CLASS_NAME}.java"
  elif [[ "$line" =~ ^05[[:space:]]+([a-zA-Z0-9_-]+)[[:space:]]+pic[[:space:]]+(.*)\. ]]; then
    RAW_ATTR="${BASH_REMATCH[1]}"
    ATTR
