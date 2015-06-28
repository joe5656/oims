/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.warehouseManagemnet.WareHouse;

/**
 *
 * @author ezouyyi
 */
public class WareHouseTable extends Db_table{
    
    
    static public String getDerivedTableName()
    {
        return "WareHouse";
    }
    
    public WareHouseTable(DataBaseManager dbM)
    {
        super("WareHouse", dbM, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("contact", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("addr", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("keeperEmployeeId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("wareHouseName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("wareHouseId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }      
    
    public Boolean NewEntry(String wareHouseName, String keeper,
            String addr, String contact)
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("wareHouseName", wareHouseName);
        valueHolder.put("keeperEmployeeId", keeper);
        valueHolder.put("addr", addr);
        valueHolder.put("contact", contact);
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert))
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;
    }
    
    public Boolean removeEntry(Integer warehouseId)
    {
        
        return Boolean.FALSE;
    }
    
    public void serializeWareHouseInstance(WareHouse wh) throws SQLException
    {
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("wareHouseName", "select");
        valueHolder.put("keeperEmployeeId", "select");
        valueHolder.put("addr", "select");
        valueHolder.put("contact", "select");
        valueHolder.put("wareHouseId", "select");
        select.fillInEntryValues(valueHolder);
        
        TableEntry where_eq = generateTableEntry();
        Map<String, String> where_eqHolder = Maps.newHashMap();
        if(wh.getId() != -1)
        {
            where_eqHolder.put("wareHouseId", wh.getId().toString());
        }
        if(wh.getName() != null)
        {
            where_eqHolder.put("wareHouseName", wh.getName());
        }
        where_eq.fillInEntryValues(valueHolder);
        
        ResultSet returnSet = super.select(select, where_eq, null, null);
        
        if(returnSet.first())
        {
            wh.setId(returnSet.getInt("wareHouseId"));
            wh.setAddr(returnSet.getString("addr"));
            wh.setKeeper(returnSet.getInt("keeperEmployeeId"));
            wh.setName("wareHouseName");
            wh.setContact(returnSet.getString("contact"));
            wh.setSyncd();
        }
    }
    
}
