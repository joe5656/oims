/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;

import oims.dataBase.MYSQL.Db_db_MYSQL;

/**
 *
 * @author ezouyyi
 */
public class Db_factory {
    static public Db_db newDataBase(Db_dataBase.db_type type, String dbName)
    {
        switch(type)
        {
            case MYSQL:
            {
                return new Db_db_MYSQL(dbName);
            }
            default:
            {
                return null;
            }
        }
    }
}
