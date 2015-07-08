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
public class ProductReciptTable  extends Db_table{
    static public String getDerivedTableName()
    {
        return "ProductReciptTable";
    }
    
    public ProductReciptTable(DataBaseManager dbm)
    {
        super("ProductReciptTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        super.registerColumn("workingHours", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("fillingProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("toppingProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("mainReciptProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("fillingReciptName", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("toppingReciptName", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("mainReciptName", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("numberOfPieces", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("productName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.TRUE,  Boolean.FALSE, null);
        super.registerColumn("productId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("reciptIndentifier", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(Integer pid, String pName, Integer number, String mainName,
            String topName, String fillingName, Integer workinghours, Boolean mainReciptByCK,
            Boolean toppingByCK, Boolean fillingByCk)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("productId", pid.toString());
        valueHolder.put("productName", pName);
        valueHolder.put("numberOfPieces", number.toString());
        valueHolder.put("mainReciptName", mainReciptByCK==true?"1":"0");
        valueHolder.put("toppingReciptName", toppingByCK==true?"1":"0");
        valueHolder.put("fillingReciptName", fillingByCk==true?"1":"0");
        valueHolder.put("mainReciptName", mainName);
        valueHolder.put("toppingReciptName", topName);
        valueHolder.put("fillingReciptName", fillingName);
        valueHolder.put("workingHours", workinghours.toString());
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        return result;
    }
    
    public SqlResultInfo update(Integer reciptId, Integer pid, String pName, Integer number, String mainName,
            String topName, String fillingName, Integer workinghours, Boolean mainReciptByCK,
            Boolean toppingByCK, Boolean fillingByCk)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(reciptId != null)
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("numberOfPieces", number.toString());
            valueHolder.put("mainReciptName", mainName);
            valueHolder.put("toppingReciptName", topName);
            valueHolder.put("fillingReciptName", fillingName);
            valueHolder.put("mainReciptName", mainReciptByCK==true?"1":"0");
            valueHolder.put("toppingReciptName", toppingByCK==true?"1":"0");
            valueHolder.put("fillingReciptName", fillingByCk==true?"1":"0");
            valueHolder.put("workingHours", workinghours.toString());

            //where 
            TableEntry where = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            valueHoldereq.put("reciptIndentifier", reciptId.toString());
            if(pid!=null)valueHoldereq.put("productId", pid.toString());
            if(pName!=null)valueHoldereq.put("productName", pName);
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
            {
                result = super.update(entryToBeUpdate, where, null, null);
            }
        }
        
        return result;
    }
    
    public SqlResultInfo query(Integer reciptId, Integer pid, String pName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        
        TableEntry entryToBeUpdate = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("productId", "selected");
        valueHolder.put("productName", "selected");
        valueHolder.put("numberOfPieces", "selected");
        valueHolder.put("mainReciptId", "selected");
        valueHolder.put("toppingReciptId", "selected");
        valueHolder.put("fillingReciptId", "selected");
        valueHolder.put("workingHours", "selected");
        valueHolder.put("mainReciptName", "selected");
        valueHolder.put("toppingReciptName", "selected");
        valueHolder.put("fillingReciptName", "selected");
        valueHolder.put("reciptIndentifier", "selected");
        
        //where 
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(reciptId!=null)valueHoldereq.put("reciptIndentifier", reciptId.toString());
        if(pid!=null)valueHoldereq.put("productId", pid.toString());
        if(pName!=null)valueHoldereq.put("productName", pName);

        if(entryToBeUpdate.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
        {
            result = super.update(entryToBeUpdate, where, null, null);
        }
        
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "productId":
            {
                return "产品编号";
            }
            case "productName":
            {
                return "产品名";
            }       
            case "numberOfPieces":
            {
                return "标准配方制作数量";
            }       
            case "mainReciptId":
            {
                return "主配方编码";
            }   
            case "toppingReciptId":
            {
                return "顶料配方编码";
            }   
            case "fillingReciptId":
            {
                return "填料配方编码";
            }    
            case "workingHours":
            {
                return "耗费工时";
            }     
            case "reciptIndentifier":
            {
                return "产品配方编码";
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
