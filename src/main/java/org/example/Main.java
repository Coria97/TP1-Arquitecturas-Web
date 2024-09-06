package org.example;


import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Main {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:mysql://localhost:3308/db_arquitectura";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

           addFacturaProducto(conn);

        } catch (SQLException | IOException e) {
            System.err.println("Error al conectarse a la base de datos:");
            e.printStackTrace();
        }
    }
    private static void addCliente(Connection connection) throws SQLException, IOException {
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/clientes.csv"));

        String insert = "INSERT INTO Cliente (idCliente,nombre,email) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        for(CSVRecord row: parser) {
            System.out.println(row.get("idCliente"));
            System.out.println(row.get("nombre"));
            System.out.println(row.get("email"));
            statement.setString(1,row.get("idCliente"));
            statement.setString(2,row.get("nombre"));
            statement.setString(3,row.get("email"));
            statement.executeUpdate();

        }


    }

    private static void addFactura(Connection connection) throws SQLException, IOException {
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/facturas.csv"));

        String insert = "INSERT INTO Factura (idFactura,idCliente) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        for(CSVRecord row: parser) {
            System.out.println(row.get("idFactura"));
            System.out.println(row.get("idCliente"));

            statement.setString(1,row.get("idFactura"));
            statement.setString(2,row.get("idCliente"));


            statement.executeUpdate();

        }


    }


    private static void addFacturaProducto(Connection connection) throws SQLException, IOException {
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/facturas-productos.csv"));

        String insert = "INSERT INTO Factura_Producto (idFactura,idProducto,cantidad) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        for(CSVRecord row: parser) {
            System.out.println(row.get("idFactura"));
            System.out.println(row.get("idProducto"));
            System.out.println(row.get("cantidad"));

            statement.setString(1,row.get("idFactura"));
            statement.setString(2,row.get("idProducto"));
            statement.setString(3,row.get("cantidad"));


            statement.executeUpdate();

        }


    }




}