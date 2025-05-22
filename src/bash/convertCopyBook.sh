#!/bin/bash

INPUT_FILE="$1"
OUTPUT_FILE=""

if [[ -z "$INPUT_FILE" ]]; then
  echo "Usage: $0 <copybook-file>"
  exit 1
fi

CLASS_NAME=""
JAVA_FIELDS=""

while IFS= read -r line; do
  # Entferne führende/trailing Leerzeichen
  line=$(echo "$line" | sed 's/^[ \t]*//;s/[ \t]*$//')

  if [[ "$line" =~ ^01[[:space:]]+([a-zA-Z0-9_-]+)\. ]]; then
    CLASS_NAME="${BASH_REMATCH[1]}"
    OUTPUT_FILE="${CLASS_NAME}.java"
  elif [[ "$line" =~ ^05[[:space:]]+([a-zA-Z0-9_-]+)[[:space:]]+pic[[:space:]]+(.*)\. ]]; then
    ATTR_NAME="${BASH_REMATCH[1]}"
    PIC_TYPE="${BASH_REMATCH[2]}"

    # Entferne Klammern
    PIC_TYPE=$(echo "$PIC_TYPE" | tr '[:upper:]' '[:lower:]')

    if [[ "$PIC_TYPE" =~ x\(([0-9]+)\) ]]; then
      LEN="${BASH_REMATCH[1]}"
      JAVA_FIELDS+="    ${ATTR_NAME} = new PicX(${LEN});\n"
    elif [[ "$PIC_TYPE" =~ 9\(([0-9]+)\) ]]; then
      LEN="${BASH_REMATCH[1]}"
      JAVA_FIELDS+="    ${ATTR_NAME} = new Pic9(${LEN});\n"
    elif [[ "$PIC_TYPE" =~ \+?9+\.9+ ]]; then
      # Float mit Vorkomma/Nachkommastellen
      IFS='.' read -r INT_PART DEC_PART <<< "$PIC_TYPE"
      INT_LEN=$(echo "$INT_PART" | tr -cd '9' | wc -c)
      DEC_LEN=$(echo "$DEC_PART" | tr -cd '9' | wc -c)
      JAVA_FIELDS+="    ${ATTR_NAME} = new Pic9(${INT_LEN}, ${DEC_LEN});\n"
    fi
  fi
done < "$INPUT_FILE"

if [[ -z "$CLASS_NAME" ]]; then
  echo "Keine gültige Copybook-Struktur gefunden."
  exit 1
fi

# Java-Klasse schreiben
{
  echo "class ${CLASS_NAME} {"
  echo -e "${JAVA_FIELDS}"
  echo "}"
} > "$OUTPUT_FILE"

echo "Konvertierung abgeschlossen: ${OUTPUT_FILE}"
