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
import oims.support.util.SqlResultInfo;

/**
 *
 * @author freda
 */
public class ErrorReportingTable  extends Db_table{
    static public String getDerivedTableName()
    {
        return "ErrorReportingTable";
    }
    
    public ErrorReportingTable(DataBaseManager dbM)
    {
        super("ErrorReportingTable", dbM, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("errorOccurTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("comments", Db_publicColumnAttribute.ATTRIBUTE_NAME.LONG_TEXT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("responserId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("responserName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("SubmitingTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("SubmitorId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("SubmitorName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("Cat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("errSpecifiedContent", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("reportId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
    }         
    
    public SqlResultInfo NewEntry(String errSpecifiedContent, String Cat, String SubmitorId,
            String SubmitorName, String comments, String errorOccurTime, String responserName,
            String responserId)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("errSpecifiedContent", errSpecifiedContent);
        valueHolder.put("Cat", Cat);
        valueHolder.put("SubmitorName", SubmitorName);
        valueHolder.put("SubmitorId", SubmitorId);
        valueHolder.put("SubmitingTime", timeFormat.format(new Date(System.currentTimeMillis())));
        valueHolder.put("SubmitorName", SubmitorName);
        valueHolder.put("responserName", responserName);
        valueHolder.put("responserId", responserId);
        valueHolder.put("comments", comments);
        valueHolder.put("errorOccurTime", errorOccurTime);
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
    /*
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
    */
    static private String EnToCh(String en)
    {   
        switch(en)
        {
            case "errorOccurTime":
            {
                return "事件发生事件";
            }
            case "comments":
            {
                return "备注";
            }
            case "responserId":
            {
                return "责任人编号";
            }       
            case "responserName":
            {
                return "责任人";
            }       
            case "SubmitingTime":
            {
                return "提交日期";
            }   
            case "SubmitorId":
            {
                return "提交人编号";
            }   
            case "SubmitorName":
            {
                return "提交人";
            }   
            case "Cat":
            {
                return "事件类别";
            }   
            case "errSpecifiedContent":
            {
                return "事件内容";
            }
            case "reportId":
            {
                return "事件编号";
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
