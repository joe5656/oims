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
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class AccountingTable  extends Db_table{
    static public String AccountingTable()
    {
        return "AccountingTable";
    }
    
    public AccountingTable(DataBaseManager dbm)
    {
        super("AccountingTable", dbm, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("description", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("status", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("approverTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("approverId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("approverName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("amount", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("cat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("ReportTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("happenTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("id", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo updateEntry(String id, String status, Boolean isApprove, 
            String approverName, String approverId)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(id == null || (isApprove!=null && isApprove && 
                (approverName == null || approverId == null)))
        {
            result.setErrInfo("参数非法，位置： AccountingTable::updateEntry");
        }
        else
        {
            TableEntry update = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("status", "status");
            if(isApprove!=null && isApprove)
            {
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String approveTime = timeFormat.format(new Date(System.currentTimeMillis()));
                valueHolder.put("approverName", approverName);
                valueHolder.put("approverId", approverId);
                valueHolder.put("approverTime", approveTime);
            }
            
            //where
            TableEntry where = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHoldereq.put("id", id);
            
            if(update.fillInEntryValues(valueHolder) && 
                    where.fillInEntryValues(valueHoldereq))
            {
                result = super.update(update, where, null, null);
            }
            else
            {
                result.setErrInfo("系统错误，位置： AccountingTable::updateEntry");
            }
        }
        return result;
    }
            
    public SqlResultInfo newEntry(String happenTime, String submitor, String submitorId,
            String amount, String cat, Boolean singleRecordeForDay, String description,
            String status)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reportTime = timeFormat.format(new Date(System.currentTimeMillis()));
        
        if(singleRecordeForDay == null || happenTime == null || submitor == null ||
                submitorId == null || amount == null || cat == null || status == null)
        {
            result.setErrInfo("信息不合法 位置：AccountingTable::newEntry");
        }
        else
        {
            try
            {
                Boolean canInsert = true;
                Double amt = Double.parseDouble(amount);
                Integer sId = Integer.parseInt(submitorId);
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
                Date    hapDate = time.parse(happenTime);
                if(singleRecordeForDay)
                {
                    TableEntry select = generateTableEntry();
                    Map<String, String> valueHolder = Maps.newHashMap();
                    valueHolder.put("id", "selected");
                    
                    //where
                    TableEntry where = generateTableEntry();
                    Map<String, String> valueHoldereq = Maps.newHashMap();
                    valueHoldereq.put("cat", cat);
                    valueHoldereq.put("happenTime", happenTime);
                    if(where.fillInEntryValues(valueHoldereq) && 
                            select.fillInEntryValues(valueHolder))
                    {
                        SqlResultInfo re = super.select(select, where, null, null);
                        if(!re.isRsEmpty())
                        {
                            canInsert = false;
                        }
                    }
                }
                
                if(canInsert)
                {
                    TableEntry entryToInsert = generateTableEntry();
                    Map<String, String> valueHolder = Maps.newHashMap();
                    valueHolder.put("happenTime", happenTime);
                    valueHolder.put("ReportTime", reportTime);
                    valueHolder.put("cat", cat);
                    valueHolder.put("amount", amount);
                    valueHolder.put("submitorId", submitorId);
                    valueHolder.put("submitorName", submitor);
                    valueHolder.put("status", status);
                    valueHolder.put("approverName", "NA");
                    valueHolder.put("approverId", "999999999");
                    valueHolder.put("approverTime", "1900-01-01");
                    if(description!=null)valueHolder.put("description", description);
                    
                    if(entryToInsert.fillInEntryValues(valueHolder))
                    {
                        result = super.insertRecord(entryToInsert);
                    }
                }
            }
            catch(Exception e)
            {
                result.setErrInfo("amount非数字 位置：AccountingTable::newEntry");
            }
        }
        return result;
    }
}
