#!/bin/bash

# Eingabe-DDL-Datei
DDL_FILE="$1"

# Funktion zum Konvertieren des SQL-Datentyps in Java-Datentyp
convert_type() {
  sql_type=$(echo "$1" | tr '[:upper:]' '[:lower:]')
  case $sql_type in
    bigint) echo "long" ;;
    varchar*|char*) echo "String" ;;
    timestamp) echo "Timestamp" ;;
    date) echo "Date" ;;
    decimal*) echo "BigDecimal" ;;
    *) echo "" ;;  # ignorieren, falls nicht relevant
  esac
}

# Extrahiere Tabellenname
TABLE_NAME=$(awk '/create table/ {gsub(/\(/,"",$3); print $3}' "$DDL_FILE" | tr -d '"')

echo "public class ${TABLE_NAME} {"

# Verarbeite jede Zeile, die eine Spalte definiert
awk '/^[[:space:]]*[a-zA-Z0-9_]+[[:space:]]+[a-zA-Z]+/ {
  gsub(/,/, "", $0)                        # Kommas entfernen
  gsub(/\(.*/, "", $2)                     # Typ-Parameter wie (255) entfernen
  field=$1
  type=$2
  # Wandlung durchführen
  cmd="convert_type " type
  "bash" -c "convert_type " type | while read java_type; do
    if [ ! -z "$java_type" ]; then
      # Feldnamen in camelCase (optional: hier nicht verändert)
      echo "    private ${java_type} ${field};"
    fi
  done
}' convert_type="$(declare -f convert_type)" "$DDL_FILE"

echo "}"