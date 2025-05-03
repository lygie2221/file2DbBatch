package de.lygie.batch;

import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.Model.DBNA;
import de.lygie.batch.Model.Versicherungsnummer;
import de.lygie.batch.helper.StaticHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BatchJobIT {

    @BeforeAll
    public static void setUp() {
        String dateiName = "/tmp/input.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiName))) {
            for (int i = 1; i <= 100000; i++) {
                //writer.write(vsnr.getVsnr());
                writer.write(StaticHelper.randomString(135));
                writer.newLine();  // Fügt einen Zeilenumbruch hinzu
            }

            DBNA dbna = new DBNA();
            PicX kennung = new PicX(4);
            kennung.setValue(StaticHelper.randomString(kennung.getLength()));
            dbna.setKennung(kennung);

            PicX fnma = new PicX(30);
            fnma.setValue("Dampf");
            dbna.setFmna(fnma);

            PicX vona = new PicX(30);
            vona.setValue("Hans");
            dbna.setVona(vona);

            PicX vosa = new PicX(30);
            vosa.setValue(StaticHelper.randomString(vosa.getLength()));
            dbna.setVosa(vosa);

            PicX nazu = new PicX(20);
            nazu.setValue(StaticHelper.randomString(nazu.getLength()));
            dbna.setNazu(nazu);

            PicX titel = new PicX(20);
            titel.setValue(StaticHelper.randomString(titel.getLength()));
            dbna.setTitel(titel);

            PicX kennzAb = new PicX(1);
            kennzAb.setValue(StaticHelper.randomString(kennzAb.getLength()));
            dbna.setKennzAb(kennzAb);

            writer.write(dbna.toString());
            writer.newLine();

        } catch (IOException e) {
                throw new RuntimeException(e);
            }




    }

    @Test
    public void testBatchJob() {
        assert (true);
    }

    public void testFolderSourceLookup() throws Exception {
        // InitialContext erzeugt den JNDI Kontext, der vom laufenden Liberty-Server bereitgestellt wird.

        // Properties mit dem nötigen InitialContext konfigurieren
        Hashtable<String, String> env = new Hashtable<>();
        // Hier müssen Sie den passenden Factory-Klassennamen für Ihre Umgebung angeben.
        // Beispiel für eine einfache Implementierung (z. B. für Tests mit Apache Tomcat):
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        // Abhängig von Ihrer Implementierung könnte hier auch ein PROVIDER_URL nötig sein.
        // env.put(Context.PROVIDER_URL, "jndi://localhost:8080");

        Context context = new InitialContext(env);
        // Beispielhafter Lookup eines DataSources; der JNDI-Name muss exakt mit dem in der server.xml übereinstimmen.
        String folder = (String) context.lookup("sourceFolder");
        assertNotNull("Folder muss vorhanden sein", folder);
    }
}
