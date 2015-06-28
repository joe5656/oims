/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.productManagement;

import oims.dataBase.tables.ProductTable;
import java.util.Map;

/**
 *
 * @author ezouyyi
 */
public class ProductManager {
    Map<Integer, ProductTable> loadedProduct_;
    
    public Boolean createNewProduct(String name, String price, String vipPrice)
    {
        return false;
    }
    
    public Boolean forceRefreshLoadList()
    {
        return false;
    }
    
    public Boolean isAllProductLoaded()
    {
        return false;
    }
}
