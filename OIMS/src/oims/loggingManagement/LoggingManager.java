/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.loggingManagement;

import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.LogTable;

/**
 *
 * @author ezouyyi
 */
public class LoggingManager {
    LogTable itsLogTable_;
    
    public LoggingManager(DataBaseManager dbm)
    {
        itsLogTable_ = new LogTable(dbm);
    }
    
    public void logging(String cat, String content)
    {
        itsLogTable_.newEntry(cat, content);
    }
}
