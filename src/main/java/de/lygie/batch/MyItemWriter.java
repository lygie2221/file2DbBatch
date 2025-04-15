package de.lygie.batch;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

public class MyItemWriter implements ItemWriter {

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // Initialisierung, falls nötig
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        // Beispielsweise werden die Items in die Konsole ausgegeben
        for (Object item : items) {
            System.out.println("Processed: " + item);
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        // Rückgabe eines Checkpoints (hier nicht notwendig)
        return null;
    }

    @Override
    public void close() throws Exception {
        // Aufräumarbeiten
    }
}