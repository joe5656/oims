/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class SyncerTable  extends Db_table{
    static public String getDerivedTableName()
    {
        return "SyncerTable";
    }
    public SyncerTable(DataBaseManager dbm)
    {
        super("SyncerTable", dbm, Db_table.Table_Type.TABLE_TYPE_MIRROR);
        super.registerColumn("versionTag", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("TableName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("tindex", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String tableName)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("TableName", tableName);
        valueHolder.put("versionTag", Long.toString(System.currentTimeMillis()));
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("插入库存信息错误，位置:WareHouseTable.NewEntry");
        }
        
        return result;        
    }
    
    private Boolean updateEntry(Db_table table)
    {
        Boolean result = Boolean.FALSE;
        
        TableEntry updateEntry = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("versionTag", Long.toString(System.currentTimeMillis()));
        
        TableEntry equalEntry = generateTableEntry();
        Map<String, String> valueHolder_eq = Maps.newHashMap();
        valueHolder_eq.put("TableName", table.getName());
        
        if(updateEntry.fillInEntryValues(valueHolder) && 
                equalEntry.fillInEntryValues(valueHolder_eq))
        {
            result = super.update(updateEntry, equalEntry, null, null).isSucceed();
        }
        return result;
    }
    
    public Set<String> compareSyncerTable(ResultSet lrs, ResultSet rrs)
    {
        Set<String> result = Sets.newConcurrentHashSet();
        Map<String,String> serializedrrs = Maps.newHashMap();
        Map<String,String> serializedlrs = Maps.newHashMap();
        
        try {
            if(rrs.first())
            {
                do
                {
                    serializedrrs.put(rrs.getString("TableName"), rrs.getString("versionTag"));
                }while(rrs.next());
            }
            if(lrs.first())
            {
                do
                {
                    serializedlrs.put(lrs.getString("TableName"), lrs.getString("versionTag"));
                }while(rrs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SyncerTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Entry<String, String> entry:serializedrrs.entrySet())
        {
            if(serializedlrs.containsKey(entry.getKey()))
            {
                if(serializedlrs.get(entry.getKey()).equals(entry.getValue()))
                {
                    continue;
                }
            }
            result.add(entry.getKey());
        }
        
        return result;
    }
    
    public void tableUpdated(Db_table table)
    {
        if(table.getTableType() == Table_Type.TABLE_TYPE_MIRROR)
        {
            if(!this.updateEntry(table))
            {
                /* try to insert one entry if update failed*/
                this.newEntry(table.getName());
            }
        }
    }
}
