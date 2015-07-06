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
    
    public UnitQuantity rawMaterialStoredInWarehouse(Integer warehouseId,Integer rawMaterialId)
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
    
    public Boolean rawMaterialCheckIn(Integer whId, String whName, Integer rawMaterialId, 
            String rawMaterialName, UnitQuantity uq)
    {
        Boolean returnValue = false;
        UnitQuantity storedQ = rawMaterialStoredInWarehouse(whId, rawMaterialId);
        if( storedQ != null)
        {
            // update
            Map<String, String> prepare = Maps.newHashMap();
            if(uq.getQuantityInUnit(storedQ.getUnit())>=0)
            {
                prepare.put("storageQuantity", "storageQuantity+"+uq.getQuantityInUnit(storedQ.getUnit()));

                TableEntry entry_update = super.generateTableEntry();
                entry_update.fillInEntryValues(prepare);

                Map<String, String> where_eq = Maps.newHashMap();
                where_eq.put("wareHouseId", whId.toString());
                where_eq.put("materialId", rawMaterialId.toString());

                TableEntry entry_eq = super.generateTableEntry();
                entry_eq.fillInEntryValues(prepare);

                returnValue = super.update(entry_update, entry_eq, null, null).isSucceed();
            }
        }
        else
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
            returnValue = super.insertRecord(entryToBeInserted).isSucceed();
        }
        
        return returnValue;
    }
    
    public Boolean rawMaterialCheckOut(Integer whid, RawMaterial rawMaterial, 
                        UnitQuantity uq)
    {
        Boolean returnValue = Boolean.FALSE;
        UnitQuantity storedQ = rawMaterialStoredInWarehouse(whid, rawMaterial.getId());
        if(storedQ != null && uq.smallerThan(storedQ))
        {
            // update
            Map<String, String> prepare = Maps.newHashMap();
            prepare.put("storageQuantity", "storageQuantity-"+uq.getQuantityInUnit(storedQ.getUnit()));

            TableEntry entry_update = super.generateTableEntry();
            entry_update.fillInEntryValues(prepare);

            Map<String, String> where_eq = Maps.newHashMap();
            where_eq.put("wareHouseId", whid.toString());
            where_eq.put("materialId", rawMaterial.getId().toString());

            TableEntry entry_eq = super.generateTableEntry();
            entry_eq.fillInEntryValues(prepare);

            returnValue = super.update(entry_update, entry_eq, null, null).isSucceed();            
        }
        return returnValue;
    }
    
    public SqlResultInfo query(Integer whId, Integer rmId)
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
        if(whId != null){whereeq.put("wareHouseId",whId.toString());}
        if(rmId != null){whereeq.put("materialId",rmId.toString());}
        
        
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
