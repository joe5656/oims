/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;

/**
 *
 * @author freda
 */
public class DataBaseFactory_MYSQL {
    static public Db_dataBase_MYSQL newDataBase(String name)
    {
        return new Db_dataBase_MYSQL(name);
    }
    static public Db_db_MYSQL newDb(String dbName)
    {
        return new Db_db_MYSQL(dbName);
    }
    
}
