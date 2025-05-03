package de.lygie.batch.Model;

public class BlobEntry {

    public BlobEntry() {
    }

    public BlobEntry(int id, int lfnr) {
        this.id = id;
        this.lfnr = lfnr;
    }

    int id;
    int lfnr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLfnr() {
        return lfnr;
    }

    public void setLfnr(int lfnr) {
        this.lfnr = lfnr;
    }
}
