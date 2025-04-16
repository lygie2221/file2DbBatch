package de.lygie.batch;

import de.lygie.batch.Model.Versicherungsnummer;
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
                Versicherungsnummer vsnr = new Versicherungsnummer();
                vsnr.generateRandomVersicherungsnummer();
                //writer.write(vsnr.getVsnr());
                writer.write("i:" + i +":"
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer()
                        + vsnr.generateRandomVersicherungsnummer())
                ;
                writer.newLine();  // Fügt einen Zeilenumbruch hinzu
            }
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
