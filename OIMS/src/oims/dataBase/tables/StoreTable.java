/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Vector;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author freda
 */
public class StoreTable  extends Db_table{
    static public String getDerivedTableName()
    {
        return "StoreTable";
    }
    
    public StoreTable(DataBaseManager dbM)
    {
        super("StoreTable", dbM, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("contact", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("addr", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("Province", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("city", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("StoreManagerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("StoreManagerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("StoreName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("StoreId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }         
    
    public SqlResultInfo NewEntry(String storeName, String StoreManagerName, Integer StoreManagerId,
            String addr, String contact, String province, String city)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("StoreName", storeName);
        valueHolder.put("StoreManagerName", StoreManagerName);
        valueHolder.put("StoreManagerId", StoreManagerId.toString());
        valueHolder.put("addr", addr);
        valueHolder.put("city", city);
        valueHolder.put("Province", province);
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
    
    public SqlResultInfo query(String storeName, String StoreManagerName, Integer StoreManagerId,
            String province, String city)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("StoreName", "selected");
        valueHolder.put("StoreManagerName", "selected");
        valueHolder.put("StoreManagerId","selected");
        valueHolder.put("addr", "selected");
        valueHolder.put("city", "selected");
        valueHolder.put("Province", "selected");
        valueHolder.put("contact", "selected");
        
        // where
        TableEntry wh = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(storeName!=null)valueHoldereq.put("StoreName", storeName);
        if(city!=null)valueHoldereq.put("city", city);
        if(province!=null)valueHoldereq.put("Province", province);
        if(StoreManagerName!=null)valueHoldereq.put("StoreManagerName", StoreManagerName);
        if(StoreManagerId!=null)valueHoldereq.put("StoreManagerId", StoreManagerId.toString());

        if(entryToBeInsert.fillInEntryValues(valueHolder) && wh.fillInEntryValues(valueHoldereq))
        {
            result = super.select(entryToBeInsert, wh, null, null);
        }
        else
        {
            result.setErrInfo("插入库存信息错误，位置:WareHouseTable.NewEntry");
        }
        
        return result;
    }
    
    public SqlResultInfo update(String storeName, String StoreManagerName, Integer StoreManagerId,
            String contact)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        if(storeName != null)
        {
            TableEntry entryToBeInsert = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            if(StoreManagerName != null && StoreManagerId != null)
            {
                valueHolder.put("StoreManagerName", StoreManagerName);
                valueHolder.put("StoreManagerId", StoreManagerId.toString());
            }
            if(contact!=null)valueHolder.put("contact", contact);

            // where
            TableEntry wh = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHoldereq.put("StoreName", storeName);

            if(entryToBeInsert.fillInEntryValues(valueHolder) && wh.fillInEntryValues(valueHoldereq))
            {
                result = super.update(entryToBeInsert, wh, null, null);
            }
            else
            {
                result.setErrInfo("插入库存信息错误，位置:WareHouseTable.NewEntry");
            }
        }
        
        
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "StoreId":
            {
                return "门店/中央厨房编码";
            }
            case "StoreName":
            {
                return "门店/中央厨房名称";
            }
            case "StoreManagerName":
            {
                return "门店/中央厨房经理";
            }       
            case "StoreManagerId":
            {
                return "门店/中央厨房经理编号";
            }       
            case "addr":
            {
                return "门店/中央厨房地址";
            }   
            case "city":
            {
                return "门店/中央厨房所在城市";
            }   
            case "Province":
            {
                return "门店/中央厨房所在省";
            }   
            case "contact":
            {
                return "联系电话";
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
}
