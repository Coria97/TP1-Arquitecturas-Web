package org.example.DAO;

import java.util.HashMap;

public interface ProductoDAO {
    //crear tabla cliente
    void createTable();

    //insert
    void insertProduct() ;

    //obtener el producto que mas recaudo

    HashMap<String,Object> getTopProduct();


}
