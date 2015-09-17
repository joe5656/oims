/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Vector;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class DetailReciptTable   extends Db_table{
    static public String getDerivedTableName()
    {
        return "DetailReciptTable";
    }
    
    public DetailReciptTable(DataBaseManager dbm)
    {
        super("DetailReciptTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        // recipt format: MaterialId1:MaterialName1:quantity1:unit1|.....|MaterialIdN:MaterialNameN:quantityN:unitN
        super.registerColumn("recipt", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("reciptName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("reciptId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String reciptName, String recipt)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("recipt", recipt);
        valueHolder.put("reciptName", reciptName);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        return result;
    }
    
    public SqlResultInfo update(String reciptName, String recipt)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(reciptName != null)
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("recipt", recipt);

            //where 
            TableEntry where = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHoldereq.put("reciptName", reciptName);
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
            {
                result = super.update(entryToBeUpdate, where, null, null);
            }
        }
        
        return result;
    }
    
    public Boolean reciptExsited(String reciptName)
    {
        Boolean result = false;
        SqlResultInfo sqlRs = query(null, reciptName);
        if(!sqlRs.isRsEmpty())
        {
            result = true;
        }
        return result;
    }
    
    public SqlResultInfo query(Integer reciptId, String reciptName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("recipt", "selected");
        valueHolder.put("reciptName", "selected");
        valueHolder.put("reciptId", "selected");
        
        //where 
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(reciptId!=null)valueHoldereq.put("reciptId", reciptId.toString());
        if(reciptName!=null)valueHoldereq.put("reciptName", reciptName);

        if(entryToBeSelect.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
        {
            result = super.select(entryToBeSelect, where, null,null);
        }
        
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "recipt":
            {
                return "配方详细信息";
            }
            case "reciptName":
            {
                return "配方名";
            }       
            case "reciptId":
            {
                return "配方编码";
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
    static public String getPrimaryKeyColNameInCh(){return EnToCh("reciptId");} 
    static public String getPrimaryKeyColNameInEng(){return "reciptId";}
    static public String getReciptNameColNameInCh(){return EnToCh("reciptName");} 
    static public String getReciptNameColNameInEng(){return "reciptName";}
}
