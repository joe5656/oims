/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.ProductPlanDataTable;
import oims.support.util.SqlResultInfo;

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
        super.registerColumn("lockerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("lockerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("planerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("planerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("lockTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("locked", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        // serialized plan detail format:
        // storeId1:productId1:quantity|storeId1:productId2:quantity|...|storeIdN:productIdN:quantity
        super.registerColumn("serializedPlanDetail", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("PlanDate", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("tindex", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newPlan(Date planDate, ProductPlanDataTable detail, Integer planerId,
            String planerName )
    {
        SqlResultInfo result = new SqlResultInfo(false);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("planerId", planerId.toString());
        valueHolder.put("planerName", planerName);
        valueHolder.put("locked", "0");
        valueHolder.put("serializedPlanDetail", detail.getSerilizedInfo());
        valueHolder.put("PlanDate", timeFormat.format(planDate));
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        
        return result;
    }
    
    public SqlResultInfo update(Date planDate, ProductPlanDataTable detail, Boolean lock,
            Integer lockerId, String lockerName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(lock == true && (lockerId == null || lockerName == null))
        {
            return result;
        }
        
        if(!planDate.before(new Date(System.currentTimeMillis()))) 
        {
            return result;
        }
        
        TableEntry updateEntry = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        if(lock == true)
        {
            valueHolder.put("locked", "1");
            valueHolder.put("lockerId", lockerId.toString());
            valueHolder.put("lockerName", lockerName);
            valueHolder.put("lockTime", timeFormat.format(new Date(System.currentTimeMillis())));
        }
        else
        {
            valueHolder.put("serializedPlanDetail", detail.getSerilizedInfo());
        }
        
        // where
        TableEntry whereeq = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("PlanDate", timeFormat.format(planDate));
        
        if(whereeq.fillInEntryValues(valueHoldereq) && updateEntry.fillInEntryValues(valueHolder))
        {
            result = super.update(updateEntry, whereeq, null, null);
        }
        return result;
    }
    
    public SqlResultInfo query(Date planDate)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        TableEntry select = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("planerId", "selected");
        valueHolder.put("planerName", "selected");
        valueHolder.put("locked", "selected");
        valueHolder.put("serializedPlanDetail", "selected");
        valueHolder.put("lockerName", "selected");
        valueHolder.put("lockerId", "selected");
        valueHolder.put("lockTime", "selected");
        
        // where
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        valueHoldereq.put("PlanDate", timeFormat.format(planDate));
        
        if(where.fillInEntryValues(valueHoldereq) && select.fillInEntryValues(valueHolder))
        {
            result = super.select(select, where, null, null);
        }
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "planerId":{return "计划人编号";}
            case "planerName":{ return "计划人";}       
            case "locked":{return "是否锁定";}       
            case "serializedPlanDetail":{return "详细信息";}   
            case "lockerName":{return "锁定人";}   
            case "lockerId":{return "锁定人编号";}
            case "lockTime":{return "锁定时间";}
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
