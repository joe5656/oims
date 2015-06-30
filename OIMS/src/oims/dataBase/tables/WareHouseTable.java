/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
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
    
    public SqlResultInfo query(String warehouseId, String keeper, String addr, 
            String contact, String warehouseName)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry selectEntry = generateTableEntry();
        Map<String, String> valueHolderSel = Maps.newHashMap();
        valueHolderSel.put("wareHouseId", "select");
        valueHolderSel.put("wareHouseName", "select");
        valueHolderSel.put("keeperEmployeeId", "select");
        valueHolderSel.put("addr", "select");
        valueHolderSel.put("contact", "select");
        selectEntry.fillInEntryValues(valueHolderSel);
        
        TableEntry Entry_eq = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("wareHouseId", warehouseId);
        valueHoldereq.put("wareHouseName", warehouseName);
        valueHoldereq.put("keeperEmployeeId", keeper);
        valueHoldereq.put("addr", addr);
        valueHoldereq.put("contact", contact);
        Entry_eq.fillInEntryValues(valueHoldereq);
        
        result = super.select(selectEntry, Entry_eq, null, null);
        
        return result;
    }
    
    public SqlResultInfo NewEntry(String wareHouseName, String keeper,
            String addr, String contact)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("wareHouseName", wareHouseName);
        valueHolder.put("keeperEmployeeId", keeper);
        valueHolder.put("addr", addr);
        valueHolder.put("contact", contact);
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
    
    public SqlResultInfo removeEntry(Integer warehouseId)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry eq = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("wareHouseId", warehouseId.toString());
        if(eq.fillInEntryValues(valueHolder))
        {
            result = super.delete(eq, null, null);
        }
        else
        {
            result.setErrInfo("插入库存信息错误，位置:WareHouseTable.NewEntry");
        }
        
        
        return result;
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
        
        ResultSet returnSet = super.select(select, where_eq, null, null).getResultSet();
        
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
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "WareHouse":
            {
                return "仓库信息表";
            }
            case "wareHouseId":
            {
                return "仓库编码";
            }       
            case "addr":
            {
                return "地址";
            }       
            case "keeperEmployeeId":
            {
                return "管理员号码";
            }   
            case "wareHouseName":
            {
                return "仓库名称";
            }   
            case "contact":
            {
                return "仓库电话";
            }   
            default:
            {
                return "错误";
            }
        }
    }
    @Override
    public void translateColumnName(Vector col)
    {
        for(int i = 0; i<col.size();i++)
        {
            col.setElementAt(EnToCh((String)col.elementAt(i)), i);
        }
    }
    
    static public String getAddreessColName(){return "addr";}
    static public String getAddreessColNameInCh(){return EnToCh("addr");}
    static public String getWarehouseNameColName(){return "wareHouseName";}
    static public String getWarehouseNameColNameInCh(){return EnToCh("wareHouseName");}   
    static public String getPrimaryKeyColName(){return "wareHouseId";}
    static public String getPrimaryKeyColNameInCh(){return EnToCh("wareHouseId");}   
}
