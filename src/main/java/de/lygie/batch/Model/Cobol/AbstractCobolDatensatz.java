package de.lygie.batch.Model.Cobol;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
                appendStringValue(value, sb);
            } catch (IllegalAccessException e) {
                sb.append(field.getName())
                        .append("=ACCESS_ERROR, ");
            }
        }
        return sb.toString();
    }


    private void appendStringValue(Object value,StringBuilder sb){
        // @TODO: eventuell Null-Werte anders behandeln
        if(null == value){
            sb.append("");
            return;
        }
        if (value instanceof PicX) {
            value = ((PicX) value).getValue();
            sb.append(value);
            return;

        }
        if (value instanceof Pic9) {
            value = value.toString();
            sb.append(value);
            return;
        }
        if (value instanceof Object[]) {
            for (Object o : (Object[]) value) {
                appendStringValue(o,sb);
            }
            return;
        }
        // 1) Jegliche Art von Array (Objekt- oder Primitiv‑Array)
        Class<?> clazz = value.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                Object elem = Array.get(value, i);
                appendStringValue(elem, sb);
            }
            return;
        }

        // 2) Alle Collections (z.B. ArrayList, LinkedList, …)
        if (value instanceof Collection<?>) {
            Collection<?> coll = (Collection<?>) value;
            for (Object o : coll) {
                appendStringValue(o, sb);
            }
            return;
        }

        // 3) Alles andere einfach per toString()
        sb.append(value.toString());

    }

    public void fromString(String input){
        int pos=0;
        Class<?> clazz = this.getClass();
        // Holt alle deklarierten Felder der aktuellen Klasse
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                pos = parseValue(value, input, pos);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int parseValue(Object value, String input, int pos) {
        if (value == null) {
            return pos;
        }

        // 1) PicX
        if (value instanceof PicX) {
            PicX pic = (PicX) value;
            int len = pic.getLength();
            pic.setValue(input.substring(pos, pos + len));
            return pos + len;
        }

        // 2) Pic9
        if (value instanceof Pic9) {
            Pic9 pic = (Pic9) value;
            int len = pic.getLength();
            pic.setValue(input.substring(pos, pos + len));
            return pos + len;
        }

        Class<?> cls = value.getClass();

        // 3) Array (Objekt- und Primitiv-Array)
        if (cls.isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                Object elem = Array.get(value, i);
                if(null != elem){
                    elem=java.lang.reflect.Array.newInstance(cls, 0).getClass();
                    ((AbstractCobolDatensatz) elem).fromString(input);
                    Array.set(value,i,elem);
                }
                pos = pos+60;
            }
            return pos;
        }

        // 4) Collection (z.B. ArrayList, LinkedList, ...)
        if (value instanceof Collection<?>) {
            for (Object elem : (Collection<?>) value) {
                pos = parseValue(elem, input, pos);
            }
            return pos;
        }


        // 6) Fallback: weglassen oder Exception werfen
        return pos;
    }


    public int fromCobolString(String input, int pos, int level) {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (level > 2) {
            return pos;
        }
        if (input.length() <= pos) {
            return pos;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Class<?> type = field.getType();
                Object value = field.get(this);

                // 1) PicX
                if (PicX.class.isAssignableFrom(type)) {
                    PicX pic = (PicX) value;
                    int len = pic.getLength();
                    pic.setValue(input.substring(pos, pos + len));
                    pos += len;
                }
                // 2) Pic9
                else if (Pic9.class.isAssignableFrom(type)) {
                    Pic9 pic = (Pic9) value;
                    int len = pic.getLength();
                    pic.setValue(input.substring(pos, pos + len));
                    pos += len;
                }
                // 3) Array von Datensätzen
                else if (type.isArray()
                        && AbstractCobolDatensatz.class.isAssignableFrom(type.getComponentType())) {
                    Object[] arr = (Object[]) value;
                    // Falls noch nicht angelegt, erstelle das Array‑Objekt
                    if (arr == null) {
                        int length = Array.getLength(value);
                        arr = (Object[]) Array.newInstance(type.getComponentType(), length);
                        field.set(this, arr);
                    }
                    level = level+1;
                    for (int i = 0; i < arr.length; i++) {
                        // Falls Element noch null, instanziere
                        if (arr[i] == null) {
                            arr[i] = type.getComponentType().newInstance();
                        }
                        // rekursiver Aufruf auf das Unter‑Objekt
                        pos = ((AbstractCobolDatensatz) arr[i]).fromCobolString(input, pos, level);
                    }
                }
                // 4) Collection von Datensätzen
                else if (value instanceof java.util.Collection<?>) {
                    java.util.Collection<?> coll = (java.util.Collection<?>) value;
                    // Hier unterscheiden, ob Du schon Einträge hast
                    level = level+1;

                    for (Object elem : coll) {
                        if (elem == null) {
                            // Instantiere neuen Eintrag (sofern nötig)
                            elem = this.getClass().getComponentType().newInstance();
                            // ...füge es zur Collection hinzu (Casting nötig)
                            ((java.util.Collection<Object>) coll).add(elem);
                        }
                        pos = ((AbstractCobolDatensatz) elem).fromCobolString(input, pos, level);
                    }
                }
                // 5) alles sonst: ignorieren oder Fehler
            } catch (Exception e) {
            }
        }
        return pos;
    }

    public String getInsertQuery(String tableName){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append(" (");
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int columns = 0;
        for (Field field : fields) {
            // Zugriff auch auf private Felder erlauben
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof PicX || value instanceof Pic9) {
                    sb.append(field.getName()).append(",");
                    columns++;
                }
            } catch (IllegalAccessException e) {

            }
        }
        sb.setLength(sb.length() - 1);
        sb.append(") values (");
        for (int i = 0; i < columns; i++) {
            sb.append("?,");
        }
        return sb.substring(0, sb.length()-1)+")";
    }

    public void bindParamsAndAdBatch(PreparedStatement stmt) throws SQLException {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int column = 1;
        for (Field field : fields) {
            // Zugriff auch auf private Felder erlauben
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof PicX) {
                    stmt.setString(column++,((PicX) value).getValue());
                }
                if (value instanceof Pic9) {
                    Long val = ((Pic9) value).getValue().longValue();
                    stmt.setBigDecimal(column++,new BigDecimal(val));

                }
            } catch (IllegalAccessException e) {

            }
        }
        stmt.addBatch();
    }


    public String getDDL(String tableName, String primaryIdField){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (");
        if(null != primaryIdField){
            sb.append(primaryIdField).append(" INT NOT NULL PRIMARY KEY AUTO_INCREMENT,");
        }

        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // Zugriff auch auf private Felder erlauben
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof PicX) {
                    sb.append("`");
                    sb.append(field.getName());
                    sb.append("` VARCHAR(" );
                    sb.append(((PicX) value).getLength());
                    sb.append("),");

                }
                if (value instanceof Pic9) {
                    value = value.toString();
                    sb.append(field.getName()).append(" DECIMAL(" ).append(((Pic9) value).getLength()).append(",0),");

                }
            } catch (IllegalAccessException e) {

            }
        }
        return sb.toString();
    }




}
