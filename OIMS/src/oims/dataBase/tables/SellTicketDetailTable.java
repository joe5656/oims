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
 * @author ezouyyi
 */
public class SellTicketDetailTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "SellTicketDetailTable";
    }
    public SellTicketDetailTable(DataBaseManager dbm)
    {
        super("SellTicketDetailTable", dbm, Table_Type.TABLE_TYPE_2_LEVEL);
        super.registerColumn("resvfield3", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield2", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield1", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("productPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("memberName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("memberId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("orderTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("productName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("id", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String ticketId, String productName, String quantity,
            String memberId, String memberName, String productPrice)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String orderTime = timeFormat.format(new Date(System.currentTimeMillis()));
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketId", ticketId);
        valueHolder.put("productName", productName);
        valueHolder.put("quantity", quantity);
        valueHolder.put("orderTime", orderTime);
        valueHolder.put("memberId", memberId);
        valueHolder.put("memberName", memberName);
        valueHolder.put("productPrice", productPrice);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("数据库连接请求失败，位置: SellTicketDetailTable.newEntry");
        }
        
        return result;        
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "id":
            {
                return "产品销售明细编号";
            }
            case "ticketId":
            {
                return "单据号码";
            }
            case "productName":
            {
                return "产品";
            }       
            case "quantity":
            {
                return "数量";
            }       
            case "orderTime":
            {
                return "购买时间";
            }   
            case "memberId":
            {
                return "会员编号";
            }   
            case "memberName":
            {
                return "会员";
            }    
            case "productPrice":
            {
                return "单价";
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
    
    static public String getPrimaryKeyColNameInCh(){return EnToCh("id");} 
    static public String getPrimaryKeyColNameInEng(){return "id";}
}
