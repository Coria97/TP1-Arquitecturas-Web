package org.example;

import java.sql.*;

import org.example.dao.mysql.MySQLClienteDAO;
import org.example.dao.mysql.MySQLFacturaDAO;
import org.example.dao.mysql.MySQLFacturaProductoDAO;
import org.example.dao.mysql.MySQLProductoDAO;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection conn = DbSingleton.getInstance();

        /** Creacion de tablas **/

        MySQLProductoDAO productoDAO = new MySQLProductoDAO(conn);
        productoDAO.createTable();

        MySQLClienteDAO clienteDAO = new MySQLClienteDAO(conn);
        clienteDAO.createTable();

        MySQLFacturaDAO facturaDAO = new MySQLFacturaDAO(conn);
        facturaDAO.createTable();

        MySQLFacturaProductoDAO facturaProductoDAO = new MySQLFacturaProductoDAO(conn);
        facturaProductoDAO.createTable();

        /** Insertar registros en las tablas **/

        productoDAO.insertProduct();

        clienteDAO.insertCliente();

        facturaDAO.insertFactura();

        facturaProductoDAO.insertFacturaProducto();

        /** Escriba un programa JDBC que retorne el producto que más recaudó. Se define
         “recaudación” como cantidad de productos vendidos multiplicado por su valor **/

        System.out.println(productoDAO.getTopProduct());

        /** Escriba un programa JDBC que imprima una lista de clientes, ordenada por a cuál se le
         facturó más **/

        clienteDAO.getListClientByBilling();
    }
}