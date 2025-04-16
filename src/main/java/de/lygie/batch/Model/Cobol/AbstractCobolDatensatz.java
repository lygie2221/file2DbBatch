package de.lygie.batch.Model.Cobol;

import java.lang.reflect.Field;

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
            } catch (IllegalAccessException e) {
            }
        }
    }


}
