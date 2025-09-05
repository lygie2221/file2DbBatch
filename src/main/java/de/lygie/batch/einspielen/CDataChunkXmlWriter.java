package de.lygie.batch.einspielen;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.api.BatchProperty;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Streamt Chunks speicherschonend in ein CDATA-Feld einer XML-Datei.
 *
 * Lebenszyklus:
 *  - open(checkpoint): Erstellt/öffnet Datei; bei Erststart schreibt Prolog + <root><largeData>
 *  - writeItems(items): Schreibt JE Item als eigene CDATA-Section (ggf. gesplittet bei "]]>")
 *  - checkpointInfo(): Gibt einfachen Restart-Status zurück
 *  - close(): Schließt </largeData></root> und Writer (nur wenn wir die Datei gestartet haben)
 */
@Named("CDataChunkXmlWriter")
@Dependent
public class CDataChunkXmlWriter implements ItemWriter {

    /** Pfad der Zieldatei (über job/step Property konfigurierbar) */
    @Inject
    @BatchProperty(name = "output")
    private String outputPath;

    /** Elementnamen konfigurierbar (optional) */

    @BatchProperty(name = "rootElement")
    private String rootElement = "root";

    @BatchProperty(name = "cdataElement")
    private String cdataElement = "largeData";

    @BatchProperty(name = "entryElement")
    private String entryElement = "chunkData";

    /** Encoding konfigurierbar (Default UTF-8) */
    @BatchProperty(name = "encoding")
    private String encodingName = "UTF-8";

    /** true -> wir haben Prolog/Starttags geschrieben und sind "Owner" des Dokuments */
    private boolean startedDocument;

    /** true -> beim Restart wurde erkannt, dass bereits begonnen wurde */
    private boolean resumed;

    private long chunkIndex = 0;

    /** Writer + XMLStreamWriter */
    private OutputStream out;
    private OutputStreamWriter osw;
    private BufferedWriter bufferedWriter;
    private XMLStreamWriter xml;

    private Charset charset;

    /** Einfacher Checkpoint */
    public static class Ckpt implements Serializable {
        private static final long serialVersionUID = 1L;
        long chunkIndex;    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Objects.requireNonNull(outputPath, "BatchProperty 'output' (Dateipfad) fehlt");
        this.charset = Charset.forName(encodingName);

        File file = new File(outputPath);
        boolean exists = file.exists() && file.length() > 0;

        // Bei Restart (checkpoint != null) hängen wir an; ansonsten neu beginnen
        boolean append = exists && checkpoint != null;

        this.out = new FileOutputStream(file, append);
        this.osw = new OutputStreamWriter(out, charset);
        this.bufferedWriter = new BufferedWriter(osw, 1 << 16); // 64KB Buffer für I/O
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        // Viele Implementierungen puffern minimal intern; echte Daten bleiben außerhalb großer Strings.
        this.xml = f.createXMLStreamWriter(bufferedWriter);

        if (append) {
            // Wir nehmen an, dass <root><largeData> bereits geschrieben wurde.
            this.startedDocument = true;
            this.resumed = true;
        } else {
            // Frischer Start: schreibe Prolog und öffnende Tags
            xml.writeStartDocument(charset.name(), "1.0");
            xml.writeCharacters("\n");
            xml.writeStartElement(rootElement);
            xml.writeCharacters("\n  ");
            xml.writeStartElement(cdataElement);
            xml.writeCharacters("\n");
            xml.flush(); // schnell auf Platte bringen
            this.startedDocument = true;
            this.resumed = false;
        }
        if (checkpoint instanceof Ckpt) {
            this.chunkIndex = ((Ckpt) checkpoint).chunkIndex;
        }

        if (!resumed) { // frischer Start: Header schreiben
            xml.writeStartDocument(charset.name(), "1.0"); xml.writeCharacters("\n");
            xml.writeStartElement(rootElement);            xml.writeCharacters("\n  ");
            xml.writeStartElement(cdataElement);           xml.writeCharacters("\n");
            xml.flush();
        }
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        if (items == null || items.isEmpty()) return;

        // <chunkData index="...">
        xml.writeCharacters("    ");
        xml.writeStartElement(entryElement);
        xml.writeAttribute("index", Long.toString(chunkIndex));
        xml.writeCharacters("\n");

        // Wichtig: StAX flushen, damit wir direkt "roh" in denselben Writer schreiben können
        xml.flush();

        // 1) CDATA-Start einmalig pro Chunk
        bufferedWriter.write("<![CDATA[\n");

        // 2) Alle Items dieses Chunks roh streamen (nur "]]>" sicher trennen)
        //    Optionaler Trenner zwischen Items:
        final char[] SEP = new char[]{'\n'}; // oder leer lassen

        boolean first = true;
        for (Object item : items) {
            if (!first && SEP.length > 0) {
                writeCDataSafeRaw(bufferedWriter, SEP, 0, SEP.length);
            }
            first = false;

            if (item == null) continue;

            if (item instanceof CharSequence) {
                CharSequence s = (CharSequence) item;
                writeCDataSafeRaw(bufferedWriter, s, 0, s.length());
            } else if (item instanceof char[]) {
                char[] c = (char[]) item;
                writeCDataSafeRaw(bufferedWriter, c, 0, c.length);
            } else if (item instanceof byte[]) {
                // Achtung Charset!
                char[] c = new String((byte[]) item, charset).toCharArray();
                writeCDataSafeRaw(bufferedWriter, c, 0, c.length);
            } else if (item instanceof Reader) {
                try (Reader r = (Reader) item) {
                    char[] buf = new char[8192];
                    int n;
                    while ((n = r.read(buf)) != -1) {
                        writeCDataSafeRaw(bufferedWriter, buf, 0, n);
                    }
                }
            } else {
                String s = String.valueOf(item);
                writeCDataSafeRaw(bufferedWriter, s, 0, s.length());
            }
        }

        // 3) CDATA-Ende einmalig pro Chunk
        bufferedWriter.write("]]>");
        bufferedWriter.flush();

        // </chunkData>
        xml.writeCharacters("\n    ");
        xml.writeEndElement();
        xml.writeCharacters("\n");
        xml.flush();
        bufferedWriter.flush();

        chunkIndex++;
    }

    /**
     * Schreibt Zeichen „roh“ in die laufende CDATA-Section und splittet nur bei „]]>“,
     * indem es „]]]]><![CDATA[>“ ausgibt. Von außen bleibt es EIN CDATA-Block (nur bei
     * tatsächlichem „]]>“ gibt es intern einen sauberen Split – das ist XML-Standard).
     */
    private static void writeCDataSafeRaw(Writer w, CharSequence s, int off, int len) throws IOException {
        int end = off + len;
        int i = off;
        while (i < end) {
            int j = indexOf(s, "]]>", i, end);
            if (j < 0) {
                // restlicher Teil direkt schreiben
                w.write(s.subSequence(i, end).toString());
                return;
            }
            if (j > i) {
                w.write(s.subSequence(i, j).toString());
            }
            // split "]]>" -> "]]]]><![CDATA[>"
            w.write("]]]]><![CDATA[>");
            i = j + 3;
        }
    }
    private static void writeCDataSafeRaw(Writer w, char[] buf, int off, int len) throws IOException {
        int end = off + len;
        int i = off;
        while (i + 2 < end) {
            if (buf[i] == ']' && buf[i+1] == ']' && buf[i+2] == '>') {
                if (i > off) w.write(buf, off, i - off);
                w.write("]]]]><![CDATA[>");
                off = i + 3;
                i = off;
            } else {
                i++;
            }
        }
        if (off < end) w.write(buf, off, end - off);
    }
    // einfacher indexOf für CharSequence (wie zuvor)
    private static int indexOf(CharSequence s, String needle, int from, int to) {
        int nlen = needle.length();
        int max = to - nlen;
        outer: for (int i = from; i <= max; i++) {
            for (int j = 0; j < nlen; j++) if (s.charAt(i + j) != needle.charAt(j)) continue outer;
            return i;
        }
        return -1;
    }

    private void writeCDataChunk(CharSequence seq) throws Exception {
        splitSafeCData(seq, 0, seq.length());
    }

    private void writeCDataChunk(char[] buf, int off, int len) throws Exception {
        splitSafeCData(buf, off, len);
    }

    /**
     * Schreibt eine CDATA-Section. Falls "]]>" im Chunk vorkommt, wird sie
     * sicher gesplittet in:
     *   "]]]]><![CDATA[>"
     * Dadurch vermeiden wir das illegale Ende in einer CDATA-Section.
     */
    private void splitSafeCData(CharSequence seq, int start, int end) throws Exception {
        int i = start;
        while (i < end) {
            int idx = indexOf(seq, "]]>", i, end);
            if (idx < 0) {
                xml.writeCData(seq.subSequence(i, end).toString()); // nur Chunk-String, nicht gesamter Inhalt
                break;
            } else {
                // Teil vor der verbotenen Sequenz
                if (idx > i) {
                    xml.writeCData(seq.subSequence(i, idx).toString());
                }
                // Schreibe die problematische Sequenz gesplittet
                xml.writeCData("]]");
                xml.writeCData(">");
                i = idx + 3; // hinter "]]>"
            }
        }
    }

    private void splitSafeCData(char[] buf, int off, int len) throws Exception {
        int end = off + len;
        int i = off;
        while (i < end) {
            int idx = indexOf(buf, i, end, ']', ']', '>');
            if (idx < 0) {
                xml.writeCData(new String(buf, i, end - i));
                break;
            } else {
                if (idx > i) {
                    xml.writeCData(new String(buf, i, idx - i));
                }
                xml.writeCData("]]");
                xml.writeCData(">");
                i = idx + 3;
            }
        }
    }


    private static int indexOf(char[] buf, int from, int to, char a, char b, char c) {
        for (int i = from; i + 2 < to; i++) {
            if (buf[i] == a && buf[i + 1] == b && buf[i + 2] == c) return i;
        }
        return -1;
    }

    @Override
    public Serializable checkpointInfo() {
        Ckpt ck = new Ckpt();
        ck.chunkIndex = this.chunkIndex;
        return ck;
    }

    @Override
    public void close() throws Exception {
        if (xml != null) {
            if (!resumed) { // wir sind „Owner“ des Dokuments
                xml.writeCharacters("  ");
                xml.writeEndElement(); // </allData> (cdataElement)
                xml.writeCharacters("\n");
                xml.writeEndElement(); // </root>
                xml.writeEndDocument();
            }
            xml.flush();
            xml.close();
        }
        if (bufferedWriter != null) bufferedWriter.close();
        if (osw != null) osw.close();
        if (out != null) out.close();
    }
}