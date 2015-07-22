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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.Db_table;
import oims.dataBase.DataBaseManager;
import oims.support.util.CommonUnit;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.UnitQuantity;
import oims.rawMaterialManagement.RawMaterial;
import oims.support.util.SqlResultInfo;
import oims.warehouseManagemnet.WareHouse;

/**
 *
 * @author ezouyyi
 */
public class StoreStackTable extends Db_table{
    
    static public String getDerivedTableName()
    {
        return "StoreStackTable";
    }
    
    public StoreStackTable(DataBaseManager dbm)
    {
        super("StoreStackTable", dbm, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("Quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("ProductName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("StoreName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("tindex", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }    
    
    public SqlResultInfo updateStoreStock(String StoreName, String ProductName, 
            Double updateNumber)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(StoreName != null && ProductName != null)
        {
            TableEntry update = super.generateTableEntry();
            Map<String, String> prepare = Maps.newHashMap();
            prepare.put("Quantity", "Quantity=Quantity+"+updateNumber.toString());
            
            TableEntry where = super.generateTableEntry();
            Map<String, String> whereeq = Maps.newHashMap();
            whereeq.put("StoreName",StoreName);
            whereeq.put("ProductName",ProductName);
            
            if(update.fillInEntryValues(prepare) && where.fillInEntryValues(whereeq) )
            {
                result = super.update(update, where, null, null);
            }
            
            if(!result.isSucceed() && "".equals(result.getErrInfo()) && updateNumber>0)
            {
                // no line affected, empty record create a new one 
                TableEntry create = super.generateTableEntry();
                Map<String, String> holder = Maps.newHashMap();
                holder.put("Quantity", updateNumber.toString());
                holder.put("StoreName", StoreName);
                holder.put("ProductName", ProductName);
                
                if(create.fillInEntryValues(holder))
                {
                    result = super.insertRecord(create);
                }
            }
        }
        return result;
    }
    
    public SqlResultInfo query(String StoreName, String ProductName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry select = super.generateTableEntry();
        Map<String, String> prepare = Maps.newHashMap();
        prepare.put("tindex", "selected");
        prepare.put("StoreName", "selected");
        prepare.put("ProductName", "selected");
        prepare.put("Quantity", "selected");
        
        TableEntry where = super.generateTableEntry();
        Map<String, String> whereeq = Maps.newHashMap();
        if(StoreName != null){whereeq.put("StoreName",StoreName);}
        if(ProductName != null){whereeq.put("ProductName",ProductName);}
        
        
        if(select.fillInEntryValues(prepare) && where.fillInEntryValues(whereeq) )
        {
            result = super.select(select, where, null, null);
        }
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "tindex":{return "货架记录编码";}       
            case "StoreName":{return "门店名称";}       
            case "ProductName":{return "产品名称";}   
            case "Quantity":{return "货架数量";}
            default:{return "错误";}
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
