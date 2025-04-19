package de.lygie.batch.Model.Cobol;

/**
 * die wesentliche Eigenschaft eines COBOL-Pictures ist die festgelegte Länge
 * während diese bei COBOL zur Compilezeit festgelegt wird, simuliert dieses
 * Interface das Verhalten zur Laufzeit.
 */
public interface ICobolPicture {
    public int getLength();
    public void setLength(int length);
}
