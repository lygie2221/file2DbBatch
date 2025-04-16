package de.lygie.batch;

import de.lygie.batch.Model.Versicherungsnummer;

import javax.batch.api.chunk.ItemWriter;
import javax.enterprise.context.Dependent;
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
public class Item2DbWriter implements ItemWriter {

    Connection conn;

    @Override
    public void open(Serializable checkpoint) throws Exception {

        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");
        conn = ds.getConnection();


    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        // Beispielsweise werden die Items in die Konsole ausgegeben


        String query = "INSERT INTO versicherte (" +
                "vsnr," +
                "sn" +
                ")" +
                " VALUES (?,?)";


        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String debug = null;
        for (Object item : items) {
            String element = item.toString();
            //Versicherungsnummer vsnr = new Versicherungsnummer(element);
            stmt.setString(1, element);
            stmt.setString(2, element.substring(0,1));
            stmt.addBatch();
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
}