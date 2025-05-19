#!/bin/bash

DDL_FILE="$1"

# Extrahiere Tabellenname (ignoriere Klammern, Sonderzeichen)
TABLE_NAME=$(awk '/create table/ {gsub(/\(/,"",$3); print $3}' "$DDL_FILE" | tr -d '"' | tr -d '`')

echo "public class ${TABLE_NAME} {"

# Verarbeite jede relevante Spaltenzeile
awk '
BEGIN {
  IGNORECASE = 1
}
# Zeilen mit Spaltendefinition
/^[[:space:]]*[a-zA-Z0-9_]+[[:space:]]+(bigint|varchar|char|timestamp|date|decimal)/ {
  gsub(/,/, "", $0)        # Komma am Ende entfernen
  field = $1
  type = tolower($2)

  # Typzuordnung
  if (type ~ /^bigint/) {
    jtype = "long"
  } else if (type ~ /^varchar/ || type ~ /^char/) {
    jtype = "String"
  } else if (type ~ /^timestamp/) {
    jtype = "Timestamp"
  } else if (type ~ /^date/) {
    jtype = "Date"
  } else if (type ~ /^decimal/) {
    jtype = "BigDecimal"
  } else {
    next
  }

  printf "    private %s %s;\n", jtype, field
}
' "$DDL_FILE"

echo "}"