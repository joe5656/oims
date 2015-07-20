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
import java.util.Objects;
import java.util.Vector;
import oims.dataBase.Db_table;
import oims.dataBase.DataBaseManager;
import oims.support.util.CommonUnit;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;
import oims.rawMaterialManagement.RawMaterial;

/**
 *
 * @author ezouyyi
 */
public class RawMaterialTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "RawMaterial";
    }
    public RawMaterialTable(DataBaseManager dbm)
    {
        super("RawMaterial", dbm, Table_Type.TABLE_TYPE_MIRROR);
        // auto managed material can not be checked out from any warehouse manually
        // they are maintained by system
        super.registerColumn("materialCat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("materialType", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("normalPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("pricingUnit", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        // not valid raw material means this material is not allowed to be brought any more
        super.registerColumn("isvalid", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("materialName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("materialId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(Double normalPrice, CommonUnit pricingUnit, String name, String type)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("materialType", type);
        valueHolder.put("normalPrice", normalPrice.toString());
        valueHolder.put("isvalid", "1");
        valueHolder.put("pricingUnit", pricingUnit.getUnitName());
        valueHolder.put("materialName", name);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("插入库存信息错误，位置:ProductTable.NewEntry");
        }
        
        return result;        
    }
    
    public void serializeRawmaterialInstance(RawMaterial rm) throws SQLException
    {
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("materialType", "select");
        valueHolder.put("normalPrice", "select");
        valueHolder.put("pricingUnit", "select");
        valueHolder.put("materialName", "select");
        valueHolder.put("isvalid", "select");
        valueHolder.put("materialId", "select");
        select.fillInEntryValues(valueHolder);
        
        TableEntry where_eq = generateTableEntry();
        Map<String, String> where_eqHolder = Maps.newHashMap();
        if(rm.getId() != -1)
        {
            where_eqHolder.put("materialId", rm.getId().toString());
        }
        if(rm.getName() != null)
        {
            where_eqHolder.put("materialName", rm.getName());
        }
        where_eq.fillInEntryValues(valueHolder);
        
        ResultSet returnSet = super.select(select, where_eq, null, null).getResultSet();
        
        if(returnSet.first())
        {
            rm.setId(returnSet.getInt("materialId"));
            rm.setPricingUnit(new CommonUnit(returnSet.getString("pricingUnit")));
            rm.setName("materialName");
            rm.setNormaiPrice(returnSet.getDouble("normalPrice"));
        }
    }
    
    public SqlResultInfo updateEntry(Integer id, String name, CommonUnit unit, 
            Boolean valid, Double price)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        if(id != null)
        {
            TableEntry select = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            if(price != null){valueHolder.put("normalPrice", price.toString());}
            if(unit != null){valueHolder.put("pricingUnit", unit.getUnitName());}
            if(name != null){valueHolder.put("materialName", name);}
            if(valid != null){valueHolder.put("isvalid", valid==true?"1":"0");}
            
            // where
            TableEntry wh = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHoldereq.put("materialId", id.toString());
            
            if(wh.fillInEntryValues(valueHoldereq) && select.fillInEntryValues(valueHolder))
            {
                result = super.update(select, wh, null, null);
            }
        }
        return result;
    }
    
    public SqlResultInfo query(Integer id, Boolean valid)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("materialType", "select");
        valueHolder.put("normalPrice", "select");
        valueHolder.put("pricingUnit", "select");
        valueHolder.put("materialName", "select");
        valueHolder.put("isvalid", "select");
        valueHolder.put("materialId", "select");
        
        // where
        TableEntry wh = generateTableEntry();
        Boolean whereNec = Boolean.FALSE;
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(id != null){valueHoldereq.put("materialId", id.toString());whereNec=true;}
        if(valid != null){valueHoldereq.put("isvalid", valid==true?"1":"0");whereNec=true;}
        
        if(wh.fillInEntryValues(valueHoldereq) && select.fillInEntryValues(valueHolder))
        {
            result = super.select(select, whereNec?wh:null, null, null);
        }
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "normalPrice":{return "参考单位价格";}
            case "pricingUnit":{return "计价单位";}   
            case "materialName":{return "原材料名称";}   
            case "isvalid":{return "是否可用";}   
            case "materialId":{return "原材料编码";}   
            case "materialType":{return "原材料类型";}
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

    static public String getUnitNameColName(){return "pricingUnit";}
    static public String getUnitNameColNameInCh(){return EnToCh("pricingUnit");}
    static public String getRmNameColName(){return "materialName";}
    static public String getRmNameColNameInCh(){return EnToCh("materialName");}   
    static public String getRmTypeColName(){return "materialType";}
    static public String getRmTypeColNameInCh(){return EnToCh("materialType");}   
    static public String getPrimaryKeyColName(){return "materialId";}
    static public String getPrimaryKeyColNameInCh(){return EnToCh("materialId");} 
}
