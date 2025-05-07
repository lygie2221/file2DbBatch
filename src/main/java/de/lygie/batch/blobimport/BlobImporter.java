package de.lygie.batch.blobimport;

import de.lygie.batch.Model.BlobEntry;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.batch.api.Batchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Named
@Dependent
public class BlobImporter implements Batchlet {

    Connection conn;

    public BlobImporter() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");
        conn = ds.getConnection();
    }

    @Override
    public String process() throws Exception {
        BlobEntry entry = new BlobEntry(111111, 12345);
        File tempfile = createTempEntryXml(entry);
        File zipFile = createZipFile(tempfile);
        //addResourceToExistingZip(zipFile, "example.xml");
        insertIntoBlob(entry, zipFile);

        return "";
    }

    @Override
    public void stop() throws Exception {

    }

    private File createZipFile(File tempfile) throws IOException {
        File zipFile = File.createTempFile("entry-", ".zip");
        zipFile.deleteOnExit();  // löscht Datei beim JVM-Exit

        try (
                FileOutputStream  fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(tempfile)
        ) {
            // Lege einen neuen Eintrag an, der im ZIP genauso heißt wie die XML-Datei
            ZipEntry entryZip = new ZipEntry("testArchiv.end");
            zos.putNextEntry(entryZip);

            // Kopiere den Inhalt der XML-Datei in den ZIP-Eintrag
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        }

        System.out.println("ZIP-Archiv erzeugt: " + zipFile.getAbsolutePath());


        return zipFile;
    }


    /**
     * Erzeugt eine XML-Datei mit folgender Struktur:
     * <entry>
     *   <table>
     *     <id>111111</id>
     *     <name><![CDATA["Dies ist der Inhalt"]]></name>
     *   </table>
     * </entry>
     *
     * und legt sie als temporäre Datei ab.
     *
     * @return File-Objekt der erstellten XML-Datei
     */
    public File createTempEntryXml(BlobEntry myEntry)
            throws ParserConfigurationException, TransformerException, IOException {

        // 1. Temporäre Datei anlegen
        File tempFile = File.createTempFile("entry-", ".xml");
        tempFile.deleteOnExit();  // löscht Datei beim JVM-Exit

        // 2. DOM-Dokument initialisieren
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db  = dbf.newDocumentBuilder();
        Document doc        = db.newDocument();

        // 3. <entry> als Wurzel
        Element entry = doc.createElement("entry");
        doc.appendChild(entry);

        // 4. <table> als Kind von <entry>
        Element table = doc.createElement("table");
        entry.appendChild(table);

        Element id = doc.createElement("id");
        id.setTextContent(Integer.toString(myEntry.getId()));
        table.appendChild(id);

        // 6. <name><![CDATA["Dies ist der Inhalt"]]></name>
        Element name = doc.createElement("name");
        CDATASection cdata = doc.createCDATASection("testCdata" + Integer.toString(myEntry.getLfnr()));
        name.appendChild(cdata);
        table.appendChild(name);

        // 7. Transformer konfigurieren (schönes, eingerücktes XML)
        TransformerFactory tf  = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "2"
        );

        // 8. In die Datei schreiben
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            DOMSource source    = new DOMSource(doc);
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
        }

        System.out.println("Temporäre XML-Datei erzeugt: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    /**
     * Fügt einer bestehenden ZIP-Datei einen Eintrag aus dem Klassenpfad hinzu.
     *
     * @param zipFile       Die bestehende ZIP-Datei, die erweitert werden soll.
     * @param resourcePath  Pfad zur Ressource im Klassen­pfad, z.B. "xml/meineDatei.xml"
     * @param entryName     Name (inkl. Pfad) im ZIP, z.B. "data/meineDatei.xml"
     * @throws IOException
     */
    public static void addResourceToExistingZip(File zipFile,
                                                String resourcePath,
                                                String entryName)
            throws IOException {
        // 1. Ressource als Stream aus dem JAR laden
        InputStream resourceStream =
                BlobImporter.class.getClassLoader().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new FileNotFoundException("Ressource '" + resourcePath + "' nicht gefunden!");
        }

        // 2. Temp-ZIP anlegen
        File tempZip = File.createTempFile("temp-zip-", ".zip");
        tempZip.deleteOnExit();

        // 3. Altes ZIP öffnen und neues schreiben
        try (ZipFile original = new ZipFile(zipFile);
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZip))) {

            // 3a. Alle bestehenden Einträge kopieren
            Enumeration<? extends ZipEntry> entries = original.entries();
            byte[] buffer = new byte[4096];
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                // Eintrag neu anlegen (Name, ggf. Zeitstempel übernehmen)
                ZipEntry copyEntry = new ZipEntry(e.getName());
                copyEntry.setTime(e.getTime());
                zos.putNextEntry(copyEntry);

                try (InputStream is = original.getInputStream(e)) {
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                zos.closeEntry();
            }

            // 3b. Neuen Eintrag aus dem Ressourcen-Stream hinzufügen
            ZipEntry newEntry = new ZipEntry(entryName);
            zos.putNextEntry(newEntry);
            int len;
            while ((len = resourceStream.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            resourceStream.close();
            zos.closeEntry();
        }

        // 4. Temporäres ZIP übers Original verschieben
        Files.move(
                tempZip.toPath(),
                zipFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );
    }

    /**
     * Fügt einer bestehenden ZIP-Datei eine weitere Datei hinzu.
     *
     * @param zipFile     Die bestehende ZIP-Datei, die erweitert werden soll.
     * @param fileToAdd   Die Datei, die hinzugefügt werden soll.
     * @param entryName   Der Name (inkl. Pfad) der Datei im ZIP, z.B. "folder/meineDatei.txt".
     * @throws IOException
     */
    public static void addFileToExistingZip(File zipFile, File fileToAdd, String entryName)
            throws IOException {
        // 1. temporäre ZIP-Datei anlegen
        File tempZip = File.createTempFile("temp-zip-", ".zip");
        tempZip.deleteOnExit();

        // 2. Altes ZIP öffnen und neuen ZipOutputStream auf Temp-ZIP
        try (ZipFile original = new ZipFile(zipFile);
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZip))) {

            // 3. Alle existierenden Einträge kopieren
            Enumeration<? extends ZipEntry> entries = original.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                // Den Eintrag neu hinzufügen (inkl. Metadaten)
                zos.putNextEntry(new ZipEntry(e.getName()));
                try (InputStream is = original.getInputStream(e)) {
                    copy(is, zos);
                }
                zos.closeEntry();
            }

            // 4. Den neuen Eintrag hinzufügen
            ZipEntry newEntry = new ZipEntry(entryName);
            zos.putNextEntry(newEntry);
            try (InputStream is = new FileInputStream(fileToAdd)) {
                copy(is, zos);
            }
            zos.closeEntry();
        }

        // 5. Original ersetzen
        Files.move(tempZip.toPath(), zipFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    // Hilfsmethode zum Kopieren
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }


    public static void addResourceToZip2(File zipFile, String filename) throws IOException {
        // 1. Lade die Ressource als InputStream
        try (InputStream resourceStream =
                     BlobImporter.class.getResourceAsStream("/static_xml/" + filename)) {
            if (resourceStream == null) {
                throw new FileNotFoundException("Ressource /xml/meineDatei.xml nicht gefunden!");
            }


            try (ZipOutputStream zos = new ZipOutputStream(
                    new FileOutputStream(zipFile))) {

                // 4. Definiere einen neuen Eintrag im ZIP
                ZipEntry entry = new ZipEntry(filename + ".xml");
                zos.putNextEntry(entry);

                // 5. Kopiere alle Bytes von der Ressource in den Zip-Eintrag
                byte[] buffer = new byte[4096];
                int len;
                while ((len = resourceStream.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                // 6. schließen des aktuellen Eintrags
                zos.closeEntry();
            }
        }
    }


    private void insertIntoBlob(BlobEntry entry, File zipFile) throws SQLException, IOException {
        String sql = "INSERT INTO myblob (id, lfnr, myblob) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(zipFile)) {

            // 3. Parameter belegen
            ps.setInt(1, entry.getId());
            ps.setInt(2, entry.getLfnr());

            // setBinaryStream(Parameter-Index, InputStream, Länge in Bytes)
            ps.setBinaryStream(3, fis, (int) zipFile.length());

            // 4. Ausführen
            int rows = ps.executeUpdate();
            System.out.println(rows + " Datensatz(e) eingefügt.");
        }
    }
}
