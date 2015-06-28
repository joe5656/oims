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
import  oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.dataBase.DataBaseManager;
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
        super.registerColumn("catId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("vipPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,    Boolean.FALSE,   Boolean.FALSE, null);
        super.registerColumn("price", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("produtName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("productId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public Boolean newEntry(String produtName, Double price, Double vipPrice, Integer catId,
                            String picUrl, String nameAbbr, Double cost)
    {
        Boolean result = Boolean.FALSE;
        
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("cost", cost.toString());
        valueHolder.put("nameAbbr", nameAbbr);
        valueHolder.put("valid", "1");
        valueHolder.put("picUrl", picUrl);
        valueHolder.put("catId", catId.toString());
        valueHolder.put("vipPrice", vipPrice.toString());
        valueHolder.put("price", price.toString());
        valueHolder.put("produtName", produtName);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert))
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;        
    }
}
