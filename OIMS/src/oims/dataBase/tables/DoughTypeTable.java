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
import java.util.Map;
import java.util.Set;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.CommonUnit;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.UnitQuantity;

/**
 *
 * @author ezouyyi
 */
public class DoughTypeTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "DoughTypeTable";
    }
    public DoughTypeTable(DataBaseManager dbm)
    {
        super("DoughTypeTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        // DoughTypeId:Factor;DoughTypeId:Factor;DoughTypeId:Factor;
        // eg: [1:1][3:0.5]
        super.registerColumn("quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("unit", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR10, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        // standard product number of one recipt quantity
        super.registerColumn("rawMaterialName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("DoughName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("index", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public Boolean newEntry(String DoughName, String rawMaterial, UnitQuantity uq)
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("DoughName", DoughName);
        valueHolder.put("rawMaterialName", rawMaterial);
        valueHolder.put("unit", uq.getUnit().getUnitName());
        valueHolder.put("quantity", uq.getQuantity().toString());
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert))
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;        
    }
    
    public Set<String> getDoughType() throws SQLException
    {
        Set<String> types = Sets.newHashSet();
       // select DoughName from
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder;
        valueHolder = Maps.newHashMap();
        valueHolder.put("DoughName", "select");
        select.fillInEntryValues(valueHolder);
        
        ResultSet result = super.select(select, null, null, null);
        if(result.first())
        {
            do
            {
                types.add(result.getString("DoughName"));
            }while(result.next());
        }
        return types;
    }
    
    public Map<String, UnitQuantity> getDoughRecipt(String doughName) throws SQLException
    {
        // select rawMaterialId,unit,quantity from
        Map<String, UnitQuantity> result = Maps.newHashMap();
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("rawMaterialName", "select");
        valueHolder.put("unit", "select");
        valueHolder.put("quantity", "select");
        select.fillInEntryValues(valueHolder);
        
        // where DoughName=doughName
        TableEntry where_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("DoughName", doughName);
        where_eq.fillInEntryValues(eq);
        
        ResultSet resultSet = super.select(select, where_eq, null, null);
        if(resultSet.first())
        {
            do
            {
                result.put(resultSet.getString("rawMaterialName"), 
                        new UnitQuantity(new CommonUnit(resultSet.getString("unit")), 
                                resultSet.getDouble("quantity")));
            }while(resultSet.next());
        }
        
        return result;
    }
}
