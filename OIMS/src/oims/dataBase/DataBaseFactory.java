/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;
import  oims.dataBase.Db_dataBase;
import  oims.dataBase.MYSQL.DataBaseFactory_MYSQL;
/**
 *
 * @author freda
 */
public class DataBaseFactory {
    static public Db_dataBase newDataBase(Db_dataBase.db_type type, String dataBaseName)
    {
        switch(type)
        {
            case MYSQL:
            {
                return DataBaseFactory_MYSQL.newDataBase(dataBaseName);
            }
            default:
            {
                return null;
            }
        }
    }
    
    static public Db_db newDb(Db_dataBase.db_type type, String dbName)
    {
        switch(type)
        {
            case MYSQL:
            {
                return DataBaseFactory_MYSQL.newDb(dbName);
            }
            default:
            {
                return null;
            }
        }
    }
}
