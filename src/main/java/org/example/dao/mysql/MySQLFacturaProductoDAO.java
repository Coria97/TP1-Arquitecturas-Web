package org.example.dao.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DbSingleton;
import org.example.dao.FacturaProductoDAO;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLFacturaProductoDAO implements FacturaProductoDAO {

    private Connection conn;

    public MySQLFacturaProductoDAO(Connection conn){
        try {
            this.conn = DbSingleton.getInstance();
        } catch (SQLException e) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertFacturaProducto() {
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/facturas-productos.csv"));

            String insert = "INSERT INTO Factura_Producto (idFactura,idProducto,cantidad) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);

            for(CSVRecord row: parser) {
                System.out.println(row.get("idFactura"));
                System.out.println(row.get("idProducto"));
                System.out.println(row.get("cantidad"));

                statement.setString(1,row.get("idFactura"));
                statement.setString(2,row.get("idProducto"));
                statement.setString(3,row.get("cantidad"));


                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
