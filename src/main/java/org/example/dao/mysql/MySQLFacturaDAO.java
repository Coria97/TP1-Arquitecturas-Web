package org.example.dao.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DbSingleton;
import org.example.dao.FacturaDAO;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLFacturaDAO implements FacturaDAO {

    private Connection conn;

    public MySQLFacturaDAO(Connection conn){
        try {
            this.conn = DbSingleton.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión", e);
        }
    }

    @Override
    public void createTable() {
        String createFacturaTable = "CREATE TABLE Factura (" +
                "idFactura INT PRIMARY KEY AUTO_INCREMENT," +
                "idCliente INT," +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                ")";
        try(PreparedStatement stmt = conn.prepareStatement(createFacturaTable)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertFactura() {

        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/facturas.csv"));

            String insert = "INSERT INTO Factura (idFactura,idCliente) VALUES (?,?)";
            try(PreparedStatement statement = conn.prepareStatement(insert)) {
                for(CSVRecord row: parser) {
                    System.out.println(row.get("idFactura"));
                    System.out.println(row.get("idCliente"));

                    statement.setString(1,row.get("idFactura"));
                    statement.setString(2,row.get("idCliente"));


                    statement.executeUpdate();

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
