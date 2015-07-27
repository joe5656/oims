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
public class StockTable extends Db_table{
    
    static public String getDerivedTableName()
    {
        return "Stock";
    }
    
    public StockTable(DataBaseManager dbm)
    {
        super("Stock", dbm, Table_Type.TABLE_TYPE_REMOTE);
        
        super.registerColumn("wareHouseName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("wareHouseId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("storageQuantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("storeUnit", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("materialId", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("materialName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("tindex", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }    
    
    public Boolean isStackEmptryForWarehouse(Integer warehouseId)
    {
        // select count(*) where warehouseid = warehouseId and storageQuantity>0
        TableEntry entry_equal = generateTableEntry();
        Map<String, String> whereClause_equal = Maps.newHashMap();
        whereClause_equal.put("wareHouseId", warehouseId.toString());
        entry_equal.fillInEntryValues(whereClause_equal);
        
        TableEntry entry_gr = generateTableEntry();
        Map<String, String> whereClause_gr = Maps.newHashMap();
        whereClause_gr.put("storageQuantity", "0");
        entry_gr.fillInEntryValues(whereClause_gr);
        
        ResultSet queryResult = super.select(null, entry_equal, entry_gr, null).getResultSet();
        
        try {
            queryResult.first();
            return 0 == queryResult.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(StockTable.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
    }
    
    public UnitQuantity rawMaterialStoredInWarehouse(Integer warehouseId,Integer rawMaterialId, String unit)
    {
        UnitQuantity returnValue = null;
        // select storageQuantity,storageUnit where warehouseid = warehouseId and storageQuantity>0
        TableEntry entry_select = generateTableEntry();
        Map<String, String> select = Maps.newHashMap();
        select.put("storageQuantity", "Selected");
        select.put("storageUnit", "Selected");
        entry_select.fillInEntryValues(select);
        
        // where
        TableEntry entry_equal = generateTableEntry();
        Map<String, String> whereClause_equal = Maps.newHashMap();
        whereClause_equal.put("wareHouseId", warehouseId.toString());
        whereClause_equal.put("materialId", rawMaterialId.toString());
        whereClause_equal.put("storageUnit", unit);
        entry_equal.fillInEntryValues(whereClause_equal);
        
        ResultSet queryResult = super.select(entry_select, entry_equal, null, null).getResultSet();
        try {
            if(queryResult.first())
            {
                Double q = queryResult.getDouble("storageQuantity");
                CommonUnit u = new CommonUnit(queryResult.getString("storageUnit"));
                returnValue = new UnitQuantity(u,q);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(StockTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnValue;
    }
    
    public SqlResultInfo rawMaterialCheckIn(String whName, Integer whId, Integer rawMaterialId,
            String rawMaterialName, UnitQuantity uq, Boolean createOnNew)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if((whName == null && whId == null) || (rawMaterialName == null && rawMaterialId == null) 
                || uq == null || createOnNew == null)
        {
            result.setErrInfo("in StockManager::rawMaterialCheckIn: 信息不完整");
            return result;
        }
        
        // check if rawMaterial with the same unit exists
        SqlResultInfo resultValue = this.query(whName, rawMaterialName, uq.getUnit().getUnitName());
        
        if(resultValue.isSucceed())
        {
            try {
                if(resultValue.getResultSet().first())
                {
                    TableEntry update = generateTableEntry();
                    Map<String, String> updateHolder = Maps.newHashMap();
                    updateHolder.put("storageQuantity", "storageQuantity+"+uq.getQuantity().toString());
                    
                    // where
                    TableEntry where = generateTableEntry();
                    Map<String, String> whereHolder = Maps.newHashMap();
                    whereHolder.put("wareHouseName", whName);
                    whereHolder.put("materialName", rawMaterialName);
                    whereHolder.put("storageUnit", uq.getUnit().getUnitName());
                    
                    if(update.fillInEntryValues(updateHolder) && where.fillInEntryValues(whereHolder))
                    {
                        result = super.update(update, where, null, null);
                    }
                }
                else if(createOnNew)
                {
                    //create
                    Map<String, String> prepare = Maps.newHashMap();
                    prepare.put("wareHouseId", whId.toString());
                    prepare.put("wareHouseName", whName);
                    prepare.put("storageQuantity", uq.getQuantity().toString());
                    prepare.put("storeUnit", uq.getUnit().getUnitName());
                    prepare.put("materialId", rawMaterialId.toString());
                    prepare.put("materialName", rawMaterialName);

                    TableEntry entryToBeInserted = super.generateTableEntry();
                    entryToBeInserted.fillInEntryValues(prepare);
                    result = super.insertRecord(entryToBeInserted);
                }
            } catch (SQLException ex) {
                Logger.getLogger(StockTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public SqlResultInfo rawMaterialCheckOut(Integer whid, Integer rawMaterialId, 
                        UnitQuantity uq)
    {
        SqlResultInfo returnValue = new SqlResultInfo(false);
        if(whid == null || uq == null || rawMaterialId == null)
        {
            returnValue.setErrInfo("in StockManager::rawMaterialCheckIn: 信息不完整");
            return returnValue;
        }
        UnitQuantity storedQ = rawMaterialStoredInWarehouse(whid, rawMaterialId, uq.getUnit().getUnitName());
        
        if(storedQ != null && storedQ.getQuantity()>= uq.getQuantity())
        {
            // update
            Map<String, String> prepare = Maps.newHashMap();
            prepare.put("storageQuantity", "storageQuantity-"+uq.getQuantity());

            TableEntry entry_update = super.generateTableEntry();
            entry_update.fillInEntryValues(prepare);

            Map<String, String> where_eq = Maps.newHashMap();
            where_eq.put("wareHouseId", whid.toString());
            where_eq.put("materialId", rawMaterialId.toString());
            where_eq.put("storeUnit", uq.getUnit().getUnitName());

            TableEntry entry_eq = super.generateTableEntry();
            entry_eq.fillInEntryValues(prepare);

            returnValue = super.update(entry_update, entry_eq, null, null);            
        }
        return returnValue;
    }
    
    public SqlResultInfo query(String wName, String rmName, String unit)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry select = super.generateTableEntry();
        Map<String, String> prepare = Maps.newHashMap();
        prepare.put("wareHouseId", "selected");
        prepare.put("wareHouseName", "selected");
        prepare.put("storageQuantity", "selected");
        prepare.put("storeUnit", "selected");
        prepare.put("materialId", "selected");
        prepare.put("materialName", "selected");
        
        TableEntry where = super.generateTableEntry();
        Map<String, String> whereeq = Maps.newHashMap();
        if(rmName != null){whereeq.put("materialId",rmName);}
        if(unit != null){whereeq.put("storeUnit",unit);}
        if(wName != null){whereeq.put("storeUnit",wName);}
        
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
            case "wareHouseId":{return "库房名";}
            case "wareHouseName":{return "库房编码";}       
            case "materialName":{return "原材料名称";}       
            case "materialId":{return "原材料编码";}   
            case "storeUnit":{return "数量单位";}
            case "storageQuantity":{return "数量";}
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
