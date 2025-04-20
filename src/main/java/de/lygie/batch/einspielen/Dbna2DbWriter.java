package de.lygie.batch.einspielen;

import de.lygie.batch.Model.DBNA;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Named
@Dependent
public class Dbna2DbWriter implements ItemWriter {

    Connection conn;

    @Inject
    @BatchProperty(name = "verfahren")
    private String verfahren;

    @Inject
    @BatchProperty(name = "liefernummer")
    private String liefernummer;

    @Inject
    @BatchProperty(name = "zieltabelle")
    private String zieltabelle;

    @Override
    public void open(Serializable checkpoint) throws Exception {

        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");
        conn = ds.getConnection();

    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        // Beispielsweise werden die Items in die Konsole ausgegeben

        String tablename = sanitize(zieltabelle);

        DBNA dbna = new DBNA();
        String query = dbna.getInsertQuery("DBNA");

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Object item : items) {
            DBNA dbnaItem = new DBNA();
            dbnaItem.fromString((String) item);
            dbnaItem.bindParamsAndAdBatch(stmt);
        }

        stmt.executeBatch();

        stmt.close();
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

    private String sanitize(String str){
        String data = null;
        if (str != null && str.length() > 0) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;
    }
}