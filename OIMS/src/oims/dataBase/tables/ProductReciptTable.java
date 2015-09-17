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
        super.registerColumn("fillingProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("singleWeightInGram", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        
        super.registerColumn("fillingProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("toppingProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("mainReciptProcessByCK", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        // 格式： 配方名称：比例
        super.registerColumn("fillingRecipt", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("toppingRecipt", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("mainRecipt", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("standardProductNumber", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("productName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.TRUE,  Boolean.FALSE, null);
        super.registerColumn("reciptIndentifier", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String pName, Integer number, String mainName, String mainFactor,
            String topName, String topFactor, String fillingName, String fillingFactor,
            Integer workinghours, Integer singleWeightInGram, Boolean mainReciptByCK,
            Boolean toppingByCK, Boolean fillingByCk)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("productName", pName);
        valueHolder.put("standardProductNumber", number.toString());
        valueHolder.put("singleWeightInGram", singleWeightInGram.toString());
        valueHolder.put("mainReciptProcessByCK", mainReciptByCK==true?"1":"0");
        valueHolder.put("toppingProcessByCK", toppingByCK==true?"1":"0");
        valueHolder.put("fillingProcessByCK", fillingByCk==true?"1":"0");
        valueHolder.put("mainRecipt", mainName+"|"+mainFactor);
        valueHolder.put("toppingRecipt", topName+"|"+topFactor);
        valueHolder.put("fillingRecipt", fillingName+"|"+fillingFactor);
        valueHolder.put("workingHours", workinghours.toString());
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        return result;
    }
    
    public SqlResultInfo update(Integer reciptId, String pName, Integer number, String mainName,
            String mainFactor, String topName, String topFactor, String fillingName, 
            String fillingFactor, Integer workinghours, Boolean mainReciptByCK,
            Boolean toppingByCK, Boolean fillingByCk, Integer singleWeightInGram)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        if(reciptId != null || pName != null)
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("standardProductNumber", number.toString());
            valueHolder.put("singleWeightInGram", singleWeightInGram.toString());
            valueHolder.put("mainRecipt", mainName+"|"+mainFactor);
            valueHolder.put("toppingRecipt", topName+"|"+topFactor);
            valueHolder.put("fillingRecipt", fillingName+"|"+fillingFactor);
            valueHolder.put("mainReciptProcessByCK", mainReciptByCK==true?"1":"0");
            valueHolder.put("toppingProcessByCK", toppingByCK==true?"1":"0");
            valueHolder.put("fillingProcessByCK", fillingByCk==true?"1":"0");
            valueHolder.put("workingHours", workinghours.toString());

            //where 
            TableEntry where = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            if(reciptId!=null)valueHoldereq.put("reciptIndentifier", reciptId.toString());
            if(pName!=null)valueHoldereq.put("productName", pName);
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
            {
                result = super.update(entryToBeUpdate, where, null, null);
            }
        }
        
        return result;
    }
    
    public Boolean reciptForProductExsited(String pName)
    {
        Boolean result = false;
        SqlResultInfo sqlRs = query(null, pName);
        if(!sqlRs.isRsEmpty())
        {
            result = true;
        }
        return result;
    }
    
    public SqlResultInfo query(Integer reciptId, String pName)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        
        TableEntry entryToBeSel = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("productName", "selected");
        valueHolder.put("standardProductNumber", "selected");
        valueHolder.put("mainRecipt", "selected");
        valueHolder.put("toppingRecipt", "selected");
        valueHolder.put("fillingRecipt", "selected");
        valueHolder.put("workingHours", "selected");
        valueHolder.put("singleWeightInGram", "selected");
        valueHolder.put("toppingProcessByCK", "selected");
        valueHolder.put("mainReciptProcessByCK", "selected");
        valueHolder.put("fillingProcessByCK", "selected");
        valueHolder.put("reciptIndentifier", "selected");
        
        //where 
        TableEntry where = generateTableEntry();
        Map<String, String> valueHoldereq = Maps.newHashMap();
        if(reciptId!=null)valueHoldereq.put("reciptIndentifier", reciptId.toString());
        if(pName!=null)valueHoldereq.put("productName", pName);

        if(entryToBeSel.fillInEntryValues(valueHolder) && where.fillInEntryValues(valueHoldereq))
        {
            result = super.select(entryToBeSel, where,null , null);
        }
        
        return result;
    }
    
    public Map unserializeRecipt(String serilizedString)
    {
        Map<String, String> result = Maps.newHashMap();
        if(serilizedString != null)
        {
            String[] rpt = serilizedString.split("|");
            if(rpt.length == 2)
            {
                result.put("reciptName", rpt[0]);
                result.put("factor", rpt[1]);
            }
        }
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "productName":{return "产品名";}       
            case "standardProductNumber":{ return "标准配方制作数量";} 
            case "singleWeightInGram":{return "单个标准重量";}
            case "mainRecipt":{return "主配方和比重";}   
            case "toppingRecipt":{ return "顶料配方和比重";}   
            case "fillingRecipt": {return "填料配方和比重";}    
            case "workingHours":{return "耗费工时";}     
            case "reciptIndentifier":{return "产品配方编码";}     
            case "mainReciptProcessByCK":{return "主配方由中央厨房加工";} 
            case "fillingProcessByCK":{return "填料配方由中央厨房加工";}
            case "toppingProcessByCK":{return "顶料配方由中央厨房加工";}
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
