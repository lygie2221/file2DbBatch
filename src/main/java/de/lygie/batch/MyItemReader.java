package de.lygie.batch;

import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.Serializable;

public class MyItemReader implements ItemReader {

    @Inject
    private JobContext jobContext;  // Optional, um den Job-Kontext zu verwenden

    private int count = 0;
    private final int maxCount = 10;  // Beispiel: Es sollen 10 Items verarbeitet werden

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // Setzt den Zähler ggf. basierend auf dem Checkpoint zurück
        count = (checkpoint != null) ? (Integer) checkpoint : 0;
    }

    @Override
    public Object readItem() throws Exception {
        // Liefert das nächste Item oder null, um das Ende anzuzeigen
        return (count < maxCount) ? count++ : null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        // Gibt den aktuellen Zählerstand als Checkpoint zurück
        return count;
    }

    @Override
    public void close() throws Exception {
        // Ressourcenfreigabe falls notwendig
    }
}
