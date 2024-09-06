package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:mysql://localhost:3308/db_arquitectura";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = conn.createStatement()) {

            System.out.println("Conexión establecida con éxito!");

            // Crear las tablas de acuerdo al esquema
            String createClienteTable = "CREATE TABLE Cliente (" +
                    "idCliente INT PRIMARY KEY AUTO_INCREMENT," +
                    "nombre VARCHAR(500)," +
                    "email VARCHAR(150)" +
                    ")";
            statement.execute(createClienteTable);

            String createFacturaTable = "CREATE TABLE Factura (" +
                    "idFactura INT PRIMARY KEY AUTO_INCREMENT," +
                    "idCliente INT," +
                    "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                    ")";
            statement.execute(createFacturaTable);

            String createProductoTable = "CREATE TABLE Producto (" +
                    "idProducto INT PRIMARY KEY AUTO_INCREMENT," +
                    "nombre VARCHAR(45)," +
                    "valor FLOAT" +
                    ")";
            statement.execute(createProductoTable);

            String createFacturaProductoTable = "CREATE TABLE Factura_Producto (" +
                    "idFactura INT," +
                    "idProducto INT," +
                    "cantidad INT," +
                    "PRIMARY KEY (idFactura, idProducto)," +
                    "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura)," +
                    "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
                    ")";
            statement.execute(createFacturaProductoTable);


            addCliente(conn);

        } catch (SQLException e) {
            System.err.println("Error al conectarse a la base de datos:");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void addCliente(Connection connection) throws SQLException, IOException {
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("productos.csv"));

        String insert = "INSERT INTO Producto (idProducto,nombre,valor) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);
        System.out.println("ANIADIENTO...");
        for(CSVRecord row: parser) {
            System.out.println(row.get("idProducto"));
            System.out.println(row.get("nombre"));
            System.out.println(row.get("valor"));
            statement.setString(1,row.get("idProducto"));
            statement.setString(2,row.get("nombre"));
            statement.setString(3,row.get("valor"));
            statement.executeUpdate();
            System.out.println("FUNCIONO");
        }


    }

}