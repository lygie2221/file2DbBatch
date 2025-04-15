package de.lygie.batch;

import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LargeFileReader implements ItemReader {

    @Inject
    private JobContext jobContext;  // Optional, um den Job-Kontext zu verwenden

    Path path;

    private int count = 1;
    private long anzahlZeilen = 100000;

    @Override
    public void open(Serializable checkpoint) throws Exception {

        String fileName = (String) new InitialContext().lookup("sourceFile");
        path = Paths.get(fileName);

        try (Stream<String> zeilenStream = Files.lines(path, StandardCharsets.UTF_8)) {
            anzahlZeilen = zeilenStream.count();
        }catch (Exception e) {

        }

        count = (checkpoint != null) ? (Integer) checkpoint : 1;
    }

    @Override
    public Object readItem() throws Exception {

        List<String> zeilenListe = null;
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            zeilenListe = stream.skip(count - 1)  // skip() erwartet die Anzahl der zu überspringenden Elemente
                    .skip(count)
                    .limit(10)      // #TODO: junkSize
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Zeile: " + count);
        count=count+10;
        return (count < anzahlZeilen) ? zeilenListe : null;
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
