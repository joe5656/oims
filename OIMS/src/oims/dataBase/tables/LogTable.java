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
import oims.support.util.SqlResultInfo;

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
    
    public SqlResultInfo newEntry(String cat, String content)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateTime = sdf.format(new Date(System.currentTimeMillis()));
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("logcat", cat);
        valueHolder.put("timestamp", dateTime);
        valueHolder.put("content", content);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("插入库存信息错误，位置:LogTable.NewEntry");
        }
        
        return result;        
    }
}
