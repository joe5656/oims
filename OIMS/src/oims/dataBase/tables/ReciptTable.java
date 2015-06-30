/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public class ReciptTable extends Db_table {
    static public String getDerivedTableName()
    {
        return "ReciptTable";
    }
    public ReciptTable(DataBaseManager dbm)
    {
        super("ReciptTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        // DoughName:Factor;DoughName:Factor;DoughName:Factor;
        // eg: [1:1][3:0.5]
        super.registerColumn("serilizedRecipt", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("singleProductWeightInGram", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR10, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        // standard product number of one recipt quantity
        super.registerColumn("standNum", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("ProductId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("index", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public Boolean newEntry(Integer ProductId, Integer standNum,
            Integer singleProductWeightInGram, Map<String, String> recipt)
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ProductId", ProductId.toString());
        valueHolder.put("standNum", standNum.toString());
        valueHolder.put("singleProductWeightInGram", singleProductWeightInGram.toString());
        valueHolder.put("serilizedRecipt", serializeRecipt(recipt));
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert).isSucceed())
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;        
    }
    
    private String serializeRecipt(Map<String, String> recipt)
    {
        String returnValue = "[NaN:NaN]";
        if(recipt != null)
        {
            Set<Entry<String, String>> reciptMapSet = recipt.entrySet();
            returnValue = "";
            Iterator<Entry<String, String>> Itr = reciptMapSet.iterator();
            while(Itr.hasNext())
            {
                Entry<String, String> tmpEntry = Itr.next();
                returnValue = returnValue + tmpEntry.getKey()+
                        ":"+ tmpEntry.getValue() +";";
            }
        }
        return returnValue;
    }
    
    private Map<String, String> unserializeRecipt(String sRecipt)
    {
        Map<String, String> recipt = Maps.newHashMap();
        if(sRecipt != null)
        {
            String[] rpt = sRecipt.split(";");

            for (String rpt1 : rpt) {
                String[] pv = rpt1.split(":");
                recipt.put(pv[0], pv[1]);
            }
        }
        return recipt;
    }
}
