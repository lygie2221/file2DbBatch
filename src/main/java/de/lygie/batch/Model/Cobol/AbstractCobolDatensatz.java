package de.lygie.batch.Model.Cobol;

import java.lang.reflect.Field;

/**
 * eine Besonderheit bei COBOL das Zusammenspiel mit
 * sequentiellen Dateien und dem Zusammenspiel mit Variablen im Programm
 * anders als bei vielen anderen Programmiersprachen haben Zeichenketten immer eine feste
 * Länge.
 * Dieser Ansatz erlaubt es Datenstrukturen wie eine Schablone über Zeichenketten zu legen.
 * Die Zuordnung von Datenstrukturen aus Dateien auf Variablen im Programm wird bei einigen typischen
 * Anwendungsfällen sehr einfach.
 * -
 * Diese Hilfsklasse bringt die nötige Logik, um das COBOL-Verhalten in die Java-Welt zu bringen.
 * Attribute der Klassen Pic9 und PicX die über feste Längenfelder verfügen können sind die Methoden toString
 * und fromString so ausgestaltet, dass ein direktes Mapping auf eine Zeichenkette beim Lesen und Schreiben
 * ähnlich wie bei COBOL ablaufen kann
 */
abstract public class AbstractCobolDatensatz {

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        // Ermittelt die Laufzeitklasse (z.B. DBNA)
        Class<?> clazz = this.getClass();
        // Holt alle deklarierten Felder der aktuellen Klasse
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // Zugriff auch auf private Felder erlauben
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                // Falls das Feld eine Instanz von PicX ist, gebe dessen inhaltlichen Wert aus
                if (value instanceof PicX) {
                    value = ((PicX) value).getValue();
                }
                if (value instanceof Pic9) {
                    value = ((Pic9) value).toString();
                }
                sb.append(value);
            } catch (IllegalAccessException e) {
                sb.append(field.getName())
                        .append("=ACCESS_ERROR, ");
            }
        }
        return sb.toString();
    }

    public void fromString(String input){
        int pos=0;
        Class<?> clazz = this.getClass();
        // Holt alle deklarierten Felder der aktuellen Klasse
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // Zugriff auch auf private Felder erlauben
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                // Falls das Feld eine Instanz von PicX ist, gebe dessen inhaltlichen Wert aus
                if (value instanceof PicX) {
                    ((PicX) value).setValue(input.substring(pos,pos + ((PicX) value).getLength()));
                    pos = pos + ((PicX) value).getLength();
                }
                if (value instanceof Pic9) {
                    ((Pic9) value).setValue(input.substring(pos,pos + ((Pic9) value).getLength()));
                    pos = pos + ((Pic9) value).getLength();
                }
            } catch (IllegalAccessException e) {
            }
        }
    }


}
