package org.example;


import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3308/db_arquitectura";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement()) {

                System.out.println("Conexión establecida con éxito!");

                // Crear las tablas de acuerdo al esquema
                /*String createClienteTable = "CREATE TABLE Cliente (" +
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
           //addFacturaProducto(conn);
            */
                //addCliente(conn);
                //addProducto(conn);
                //addFactura(conn);

                //addFacturaProducto(conn);
                HashMap<String, Object> product_data = getTopProduct(conn);
                for(Map.Entry<String, Object> entry : product_data.entrySet())
                    System.out.print(entry.getKey() + " : " + entry.getValue() + "\n");

                //getTClientList(conn);

        } catch (SQLException e) {
            System.err.println("Error al conectarse a la base de datos:");
            e.printStackTrace();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
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

    private static void addProducto(Connection connection) throws SQLException, IOException {
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/productos.csv"));

        String insert = "INSERT INTO Producto (idProducto,nombre,valor) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        for(CSVRecord row: parser) {
            System.out.println(row.get("idProducto"));
            System.out.println(row.get("nombre"));
            System.out.println(row.get("valor"));
            statement.setString(1,row.get("idProducto"));
            statement.setString(2,row.get("nombre"));
            statement.setString(3,row.get("valor"));
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

    private static  HashMap<String, Object> getTopProduct(Connection conn) throws SQLException, IOException {
        String query = "SELECT p.idProducto, p.nombre, p.valor, fp.total_vendido FROM Producto p " +
                "JOIN ( " +
                "    SELECT fp.idProducto, (SUM(fp.cantidad) * p.valor) AS total_vendido " +
                "    FROM Factura_Producto fp " +
                "    JOIN Producto p ON p.idProducto = fp.idProducto " +
                "    GROUP BY fp.idProducto " +
                "    ORDER BY total_vendido DESC " +
                ") fp ON fp.idProducto = p.idProducto " +
                "LIMIT 1;";

        HashMap<String, Object> productData = new HashMap<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                productData.put("idProducto", rs.getInt("idProducto"));
                productData.put("nombre", rs.getString("nombre"));
                productData.put("valor", rs.getString("valor"));
                productData.put("total_vendido", rs.getString("total_vendido"));
            }
        }

        return productData;
    }

    private static void getTClientList(Connection conn) throws SQLException, IOException {
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
        }
    }


}