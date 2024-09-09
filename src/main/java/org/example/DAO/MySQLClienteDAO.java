package org.example.DAO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MySQLClienteDAO implements ClienteDAO {
    private Connection conn;

    public MySQLClienteDAO(Connection conn){
        try {
            this.conn = DbSingleton.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión", e);
        }
    }


    @Override
    public void createTable() {
        String createClienteTable = "CREATE TABLE Cliente (" +
                "idCliente INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(500)," +
                "email VARCHAR(150)" +
                ")";
        try(PreparedStatement statement = conn.prepareStatement(createClienteTable)){
           statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertCliente() {
        try {
           CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/clientes.csv"));

            String insert = "INSERT INTO cliente (idCliente,nombre,email) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);

            for (CSVRecord row : parser) {
                System.out.println(row.get("idCliente"));
                System.out.println(row.get("nombre"));
                System.out.println(row.get("email"));
                statement.setString(1, row.get("idCliente"));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("email"));
                statement.executeUpdate();

            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void getListClientByBilling() {
        String query = "SELECT c.idCliente, c.nombre, SUM(p.valor * fp.cantidad) AS total_facturado " +
                "FROM Cliente c " +
                "JOIN Factura f ON c.idCliente = f.idCliente " +
                "JOIN Factura_Producto fp ON f.idFactura = fp.idFactura " +
                "JOIN Producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY c.idCliente " +
                "ORDER BY total_facturado DESC;";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.print("ID Cliente: " + rs.getInt("idCliente"));
                System.out.print(", Nombre: " + rs.getString("nombre"));
                System.out.println(", Total Facturado: " + rs.getString("total_facturado"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
