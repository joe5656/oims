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
import oims.dataBase.Db_table;
import oims.dataBase.DataBaseManager;
import oims.support.util.CommonUnit;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;
import oims.warehouseManagemnet.RawMaterial;

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
        super.registerColumn("isAutoManageMaterial", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("normalPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("pricingUnit", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        // not valid raw material means this material is not allowed to be brought any more
        super.registerColumn("isvalid", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("materialName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("materialId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(Double normalPrice, CommonUnit pricingUnit, String name)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
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
            rm.setSyncd();
        }
    }
    
    public Boolean disableRawMaterial(RawMaterial rm)
    {
        Boolean returnValue = Boolean.FALSE;
        if(rm.isSyncd())
        {
            TableEntry set = generateTableEntry();
            Map<String, String> set_value = Maps.newHashMap();
            set_value.put("isvalid", "0");
            set.fillInEntryValues(set_value);
            
            TableEntry where = generateTableEntry();
            Map<String, String> where_eq = Maps.newHashMap();
            where_eq.put("materialId", rm.getId().toString());
            where.fillInEntryValues(set_value);
            
            returnValue = super.update(set, where, null, null).isSucceed();
            if(Objects.equals(Boolean.TRUE, returnValue))
            {
                rm.setValid(Boolean.FALSE);
            }
        }
        
        return returnValue;
    }
    
    public Boolean enableRawMaterial(RawMaterial rm)
    {
        Boolean returnValue = Boolean.FALSE;
        if(rm.isSyncd())
        {
            TableEntry set = generateTableEntry();
            Map<String, String> set_value = Maps.newHashMap();
            set_value.put("isvalid", "1");
            set.fillInEntryValues(set_value);
            
            TableEntry where = generateTableEntry();
            Map<String, String> where_eq = Maps.newHashMap();
            where_eq.put("materialId", rm.getId().toString());
            where.fillInEntryValues(set_value);
            
            returnValue = super.update(set, where, null, null).isSucceed();
            if(Objects.equals(Boolean.TRUE, returnValue))
            {
                rm.setValid(Boolean.TRUE);
            }
        }
        
        return returnValue;
    }
}
