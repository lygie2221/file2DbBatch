package de.lygie.batch.einspielen;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Named
@Dependent
public class LargeFileReader implements ItemReader {

    @Inject
    private JobContext jobContext;  // Optional, um den Job-Kontext zu verwenden

    Path path;

    private BufferedReader reader;

    private boolean hasNext;
    String nextLine;
    private long aktuelleZeile;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Instant timestamp = Instant.now();
        System.out.println("Start: GMT/UTC Time: " + timestamp);
        String fileName = (String) new InitialContext().lookup("sourceFile");
        path = Paths.get(fileName);

        reader = new BufferedReader(new FileReader(fileName));

        // Falls ein Checkpoint vorhanden ist, müssen wir die bereits gelesenen Zeilen überspringen.
        if (checkpoint != null) {
            aktuelleZeile = (Long) checkpoint;
            for (long i = 0; i < aktuelleZeile; i++) {
                // Überspringe die bereits verarbeiteten Zeilen
                reader.readLine();
            }
        }
        nextLine = reader.readLine();

    }

    /**
     * Liest eine einzelne Zeile und erhöht den internen Zähler.
     * Wenn das Ende der Datei erreicht ist, wird null zurückgegeben,
     * was dem Batch-Framework signalisiert, dass keine weiteren Items vorliegen.
     */
    @Override
    public Object readItem() throws Exception {
        String line = nextLine;
        nextLine = reader.readLine();
        if (nextLine != null) {
            aktuelleZeile++;
            hasNext=true;
            return line;
        }
        Instant timestamp = Instant.now();
        System.out.println("Ende: GMT/UTC Time: " + timestamp);
        return null;
    }


    @Override
    public Serializable checkpointInfo() throws Exception {
        // Gibt den aktuellen Zählerstand als Checkpoint zurück
        return aktuelleZeile;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
