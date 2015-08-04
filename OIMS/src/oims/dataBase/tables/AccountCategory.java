/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class AccountCategory extends Db_table{
    static public String AccountCategory()
    {
        return "AccountCategory";
    }
    
    public AccountCategory(DataBaseManager dbm)
    {
        super("AccountCategory", dbm, Db_table.Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("catActive", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("parentCatName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("CatName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("id", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }    
    
    public SqlResultInfo newEntry(String CatName, String parentCatName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("CatName", CatName);
        valueHolder.put("parentCatName", parentCatName!=null?parentCatName:"TopLevel");
        valueHolder.put("catActive", "1");
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        return result;
    }
    
    public SqlResultInfo updateEntry(String CatName, Boolean isActive)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(CatName == null || isActive == null)
        {
            result.setErrInfo("输入不合法，位置：AccountCategory:updateEntry");
        }
        else
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("catActive", isActive?"1":"0");
            
            //where
            TableEntry where = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHolder.put("CatName", CatName);
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && 
                    where.fillInEntryValues(valueHoldereq))
            {
                return super.update(entryToBeUpdate, where, null, null);
            }
            else
            {
                result.setErrInfo("系统错误，位置：AccountCategory:updateEntry");
            }
        }
        
        return result;
    }
    
    public SqlResultInfo query(String parentCat, Boolean topLevel)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(topLevel)
        {
            valueHoldereq.put("parentCatName", "TopLevel");
        }
        else
        {
            if(parentCat != null)
            {
                valueHoldereq.put("parentCatName", parentCat);
                
                TableEntry select = generateTableEntry();
                Map<String, String> valueHolder = Maps.newHashMap();
                valueHolder.put("CatName", "selected");
                if(where.fillInEntryValues(valueHoldereq) &&
                        select.fillInEntryValues(valueHolder))
                {
                    return super.select(select, where, null, null);
                }
                else
                {
                    result.setErrInfo("系统错误，位置：AccountCategory:query");
                }
            }
            else
            {
                result.setErrInfo("输入不合法，位置：AccountCategory:query");
            }
        }
        return result;
    }
}
