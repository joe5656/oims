/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public class ProducingPlanTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "ProducingPlanTable";
    }
    public ProducingPlanTable(DataBaseManager dbm)
    {
        super("ProducingPlanTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        // DoughTypeId:Factor;DoughTypeId:Factor;DoughTypeId:Factor;
        // eg: [1:1][3:0.5]
        super.registerColumn("quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ProductId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("PlanDate", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("index", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public Map<Integer, Integer> planForDate(Date date) throws SQLException
    {
        Map<Integer, Integer> productList = Maps.newHashMap();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        //select productId, quantity
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ProductId", "select");
        valueHolder.put("quantity", "select");
        select.fillInEntryValues(valueHolder);
        
        // where PlanDate = date
        TableEntry where = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("PlanDate", timeFormat.format(date));
        where.fillInEntryValues(valueHolder);
        
        ResultSet result = super.select(select, where, null, null);
        if(result.first())
        {
            do
            {
                productList.put(result.getInt("ProductId"), result.getInt("quantity"));
            }while(result.next());
        }
        return productList;
    }
    
    public Boolean newPlan(Date planDate, Map<Integer, Integer> productList)
    {
        Boolean result = Boolean.FALSE;
        Iterator<Entry<Integer, Integer>> itr = productList.entrySet().iterator();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(itr.hasNext())
        {
            Entry<Integer, Integer> entry = itr.next();
            TableEntry entryToBeInsert = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("ProductId", entry.getKey().toString());
            valueHolder.put("quantity", entry.getValue().toString());
            valueHolder.put("PlanDate", timeFormat.format(planDate));
            entryToBeInsert.fillInEntryValues(valueHolder);
            result |= super.insertRecord(entryToBeInsert);
        }
        return result;
    }
    
    public Boolean modifyPlan(Date planDate, Map<Integer, Integer> needToModify, Boolean delete)
    {
        Boolean result = Boolean.FALSE;
        if(!planDate.before(new Date(System.currentTimeMillis()))) {
            return result;
        }
        
        Iterator<Entry<Integer, Integer>> itr = needToModify.entrySet().iterator();
        if(itr.hasNext())
        {
            // modify quantity
            Entry<Integer, Integer> entry = itr.next();
            TableEntry entryToBeInsert = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("quantity", entry.getValue().toString());
            entryToBeInsert.fillInEntryValues(valueHolder);
            
            // where index=index
            TableEntry where_eq = generateTableEntry();
            Map<String, String> eq = Maps.newHashMap();
            eq.put("index", entry.getKey().toString());
            where_eq.fillInEntryValues(valueHolder);
            if(delete)
            {
                result |= super.delete(null, where_eq, null);
            }
            else
            {
                result |= super.update(entryToBeInsert, where_eq, null, null);
            }
        }
        return result;
    }
}
