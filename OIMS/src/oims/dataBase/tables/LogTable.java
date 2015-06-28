/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.CommonUnit;
import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public class LogTable extends Db_table {
    static public String getDerivedTableName()
    {
        return "LogTable";
    }
    public LogTable(DataBaseManager dbm)
    {
        super("LogTable", dbm, Table_Type.TABLE_TYPE_2_LEVEL);
        super.registerColumn("content", Db_publicColumnAttribute.ATTRIBUTE_NAME.LONG_TEXT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        // not valid raw material means this material is not allowed to be brought any more
        super.registerColumn("timestamp", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATATIME,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("logcat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("logId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public Boolean newEntry(String cat, String content)
    {
        Boolean result = Boolean.FALSE;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateTime = sdf.format(new Date(System.currentTimeMillis()));
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("logcat", cat);
        valueHolder.put("timestamp", dateTime);
        valueHolder.put("content", content);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert))
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;        
    }
}
