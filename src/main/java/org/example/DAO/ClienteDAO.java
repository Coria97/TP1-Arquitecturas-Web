package org.example.DAO;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface ClienteDAO {

    //crear tabla cliente
    void createTable();

    //insert
    void insertCliente() ;

    //obtener todos los clientes ordenados descendentemente por su facturacion

    void getListClientByBilling();



}
