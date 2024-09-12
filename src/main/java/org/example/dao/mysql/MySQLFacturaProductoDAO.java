package org.example.dao.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.dao.GenericDAO;

import java.io.FileReader;
import java.sql.*;

public class MySQLFacturaProductoDAO implements GenericDAO {

    private Connection conn;

    public MySQLFacturaProductoDAO(Connection conn){
        try {
            this.conn = conn;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la conexión", e);
        }
    }

    @Override
    public void createTable() {
        String createFacturaProductoTable = "CREATE TABLE Factura_Producto (" +
                "idFactura INT," +
                "idProducto INT," +
                "cantidad INT," +
                "PRIMARY KEY (idFactura, idProducto)," +
                "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura)," +
                "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
                ")";
        try(PreparedStatement statement = conn.prepareStatement(createFacturaProductoTable)) {
            statement.executeUpdate();
        } catch (SQLSyntaxErrorException e){
            System.out.println("Ya existe la tabla Factura_Producto");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertRegister() {
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/facturas-productos.csv"));

            String insert = "INSERT INTO Factura_Producto (idFactura,idProducto,cantidad) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);

            for(CSVRecord row: parser) {

                statement.setString(1,row.get("idFactura"));
                statement.setString(2,row.get("idProducto"));
                statement.setString(3,row.get("cantidad"));

                statement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("La tabla Factura-Producto ya esta cargada");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
