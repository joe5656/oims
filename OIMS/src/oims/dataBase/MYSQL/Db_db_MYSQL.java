/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;
import  oims.dataBase.Db_db;
/**
 *
 * @author freda
 */
public class Db_db_MYSQL implements Db_db{
    private String dbName_;
    public Db_db_MYSQL(String dbName)
    {
        dbName_ = dbName;
    }
    
    public String getName()
    {
        return dbName_;
    }
}
