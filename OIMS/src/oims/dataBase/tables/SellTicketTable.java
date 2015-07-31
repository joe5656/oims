/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.employeeManager.Employee;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public class SellTicketTable extends Db_table{
    static public String getDerivedTableName()
    {
        return "SellTicketTable";
    }
    
    public SellTicketTable(DataBaseManager dbm)
    {
        super("SellTicketTable", dbm, Table_Type.TABLE_TYPE_2_LEVEL);
        super.registerColumn("resvfield3", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield2", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("resvfield1", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("discountType", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("salerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("salerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("paymentMethod", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("orderTime", Db_publicColumnAttribute.ATTRIBUTE_NAME.TIME,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("amount", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("memberName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("originAmount", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("memberId", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("storeName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
    
    public SqlResultInfo newEntry(String StoreName, String memberId, Double originPrice,
            Double amount, String salerName, String discntType, String salerId, String memberName)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderTime = timeFormat.format(new Date(System.currentTimeMillis()));
        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("storeName", StoreName);
        valueHolder.put("memberId", memberId);
        valueHolder.put("originAmount", originPrice.toString());
        valueHolder.put("amount", amount.toString());
        valueHolder.put("orderTime", timeFormat.format(orderTime));
        valueHolder.put("paymentMethod", "cash");
        valueHolder.put("salerName", salerName);
        valueHolder.put("salerId", salerId);
        valueHolder.put("discountType", discntType);
        valueHolder.put("memberName", memberName);
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        else
        {
            result.setErrInfo("数据库连接请求失败，位置: SellTicketTable.newEntry");
        }
        
        return result;        
    }
    
    
    static private String EnToCh(String en)
    {       
        switch(en)
        {
            case "storeName":
            {
                return "出售门店";
            }
            case "memberId":
            {
                return "会员号码";
            }       
            case "originAmount":
            {
                return "原始总价";
            }       
            case "amount":
            {
                return "折扣后总价";
            }   
            case "orderTime":
            {
                return "购买时间";
            }   
            case "paymentMethod":
            {
                return "付款方式";
            }    
            case "salerName":
            {
                return "销售员";
            }     
            case "salerId":
            {
                return "销售员编号";
            }        
            case "discountType":
            {
                return "折扣类型";
            }              
            case "memberName":
            {
                return "会员";
            }              
            case "ticketId":
            {
                return "单据编号";
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
    
    static public String getPrimaryKeyColNameInCh(){return EnToCh("ticketId");} 
    static public String getPrimaryKeyColNameInEng(){return "ticketId";}
}
