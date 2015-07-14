/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Vector;
import  oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.dataBase.DataBaseManager;
import oims.support.util.SqlResultInfo;
/**
 *
 * @author ezouyyi
 */
public class ProductTable extends Db_table{
    
    static public String getDerivedTableName()
    {
        return "Product";
    }
    
    public ProductTable(DataBaseManager dbm)
    {
        super("Product", dbm, Table_Type.TABLE_TYPE_MIRROR);
        super.registerColumn("valid", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("cost", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("nameAbbr", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("picUrl", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("cat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("vipPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("price", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("produtName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("productId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String produtName, Double price, Double vipPrice, String cat,
                            String picUrl, String nameAbbr, Double cost)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("cost", cost.toString());
        valueHolder.put("nameAbbr", nameAbbr);
        valueHolder.put("valid", "1");
        valueHolder.put("picUrl", picUrl);
        valueHolder.put("cat", cat);
        valueHolder.put("vipPrice", vipPrice.toString());
        valueHolder.put("price", price.toString());
        valueHolder.put("produtName", produtName);
        
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
    
    public SqlResultInfo update(Integer productId, Double price, Double vipPrice,
            Boolean valid, String name, Double cost, String nameAbbr, String picUrl,
            String cat)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        if(productId!=null)
        {
            TableEntry entryToUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("cost", cost.toString());
            valueHolder.put("nameAbbr", nameAbbr);
            valueHolder.put("valid", valid?"1":"0");
            valueHolder.put("picUrl", picUrl);
            valueHolder.put("cat", cat);
            valueHolder.put("vipPrice", vipPrice.toString());
            valueHolder.put("price", price.toString());
            valueHolder.put("produtName", name);
            
            //where
            TableEntry wheq = generateTableEntry();
            Map<String, String> whereeq = Maps.newHashMap();
            whereeq.put("productId", productId.toString());
            
            if(entryToUpdate.fillInEntryValues(valueHolder) && wheq.fillInEntryValues(whereeq))
            {
                result = super.update(entryToUpdate, wheq, null, null);
            }
        }
        return result; 
    }
    
    public SqlResultInfo query(Integer productId, Boolean valid,  String nameAbbr)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        if(productId!=null)
        {
            TableEntry entryToSelect = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            valueHolder.put("cost", "selected");
            valueHolder.put("nameAbbr", "selected");
            valueHolder.put("valid", "selected");
            valueHolder.put("picUrl", "selected");
            valueHolder.put("cat", "selected");
            valueHolder.put("vipPrice", "selected");
            valueHolder.put("price", "selected");
            valueHolder.put("produtName", "selected");
            
            //where
            TableEntry wheq = generateTableEntry();
            Map<String, String> whereeq = Maps.newHashMap();
            whereeq.put("productId", productId.toString());
            whereeq.put("valid", valid?"1":"0");
            
            if(entryToSelect.fillInEntryValues(valueHolder) && wheq.fillInEntryValues(whereeq))
            {
                result = super.select(entryToSelect, wheq, null, null);
            }
        }
        return result; 
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "cost":{return "成本";}
            case "nameAbbr":{return "名称缩写";}   
            case "cat":{return "类别";}   
            case "valid":{return "是否可用";}   
            case "price":{return "价格";}   
            case "vipPrice":{return "会员价";}
            case "produtName":{return "商品名称";}
            case "productId":{return "商品编码";}
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
    
    static public String getPrimaryKeyColName(){return "productId";}
    static public String getPrimaryKeyColNameInCh(){return EnToCh("productId");}   
}
