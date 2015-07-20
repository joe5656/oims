/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.SqlResultInfo;
import oims.support.util.UnitQuantity;
import oims.ticketSystem.Ticket;
import oims.ticketSystem.Ticket.TicketType;

/**
 *
 * @author ezouyyi
 */
public class CiCoTicketTable extends Db_table {
    static public String getDerivedTableName()
    {
        return "CiCoTicketTable";
    }
    public CiCoTicketTable(DataBaseManager dbm)
    {
        super("CiCoTicketTable", dbm, Table_Type.TABLE_TYPE_REMOTE);
        super.registerColumn("Tickethistory", Db_publicColumnAttribute.ATTRIBUTE_NAME.LONG_TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        // for CI ticket requestDate is the date the cargos are supposed arrival date
        // for CO ticket requestDate is the date that raw materials supposed to be recieved by requester
        super.registerColumn("requestDate", Db_publicColumnAttribute.ATTRIBUTE_NAME.DATE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("delvieryFee", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("totalPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("unitPrice", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("Unit", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("Quantity", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("rawMaterialName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("currentOwnerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("currentOwnerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        // if it's CI ticket, for contains value of warehouseId; if it's CO ticket, for is deptId
        super.registerColumn("forName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("forId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("submitorId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("ticketType", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("status", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }   
    

    public SqlResultInfo NewEntry(Ticket.TicketType ticketType, Integer submitorId, String submitorName,
            Integer reciever, String recieverName, String rawMName, UnitQuantity quantity, String unitPrice,
            String totalPrice, String deliveryFee, Date requestDate)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String historyString = "\nTicket submitted at "+timeFormat.format(new Date(System.currentTimeMillis()));
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketType", ticketType.toString());
        valueHolder.put("submitorId", submitorId.toString());
        valueHolder.put("submitorName", submitorName);
        valueHolder.put("rawMaterialName", rawMName);
        valueHolder.put("Quantity", quantity.getQuantity().toString());
        valueHolder.put("Unit", quantity.getUnit().getUnitName());
        valueHolder.put("forName", recieverName);
        valueHolder.put("forId", reciever.toString());
        valueHolder.put("requestDate", (requestDate==null?"1900-01-01":timeFormat.format(requestDate)));
        valueHolder.put("delvieryFee", deliveryFee);
        valueHolder.put("currentOwnerId", submitorId.toString());
        valueHolder.put("currentOwnerName", submitorName);
        valueHolder.put("Tickethistory", historyString);
        if(ticketType == TicketType.WAREHOUSETICKET_CI)
        {
            valueHolder.put("unitPrice", unitPrice);
            valueHolder.put("totalPrice", totalPrice);
            valueHolder.put("status", Ticket.CiTicketStatus.CI_SUBMITTED.toString());
        }
        else
        {
            valueHolder.put("status", Ticket.CoTicketStatus.CO_SUMITTED.toString());
        }
        
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            result = super.insertRecord(entryToBeInsert);
        }
        
        return result;
    }    
    
    public SqlResultInfo querySummary(Ticket.TicketType ticketType, Integer owner, 
            Integer submitor, Ticket.CiTicketStatus ciStatus, Ticket.CoTicketStatus coStatus)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        if(ticketType != null)
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            
            valueHolder.put("ticketType",  "selected");
            valueHolder.put("requestDate", "selected");          
            valueHolder.put("Unit", "selected");              
            valueHolder.put("Quantity", "selected");        
            valueHolder.put("rawMaterialName", "selected");   
            valueHolder.put("currentOwnerName", "selected");      
            valueHolder.put("forName", "selected");                
            valueHolder.put("status", "selected");                
            valueHolder.put("ticketId", "selected");    
            
            //where
            TableEntry whereeq = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            if(owner!=null){valueHoldereq.put("currentOwnerId", owner.toString());} 
            if(submitor!=null){valueHoldereq.put("submitorId", submitor.toString());}
            switch(ticketType)
            {
                case WAREHOUSETICKET_CI:
                {
                    if(ciStatus!=null){valueHoldereq.put("status", ciStatus.toString());}
                    break;
                }
                case WAREHOUSETICKET_CO:
                {
                    if(coStatus!=null){valueHoldereq.put("status", coStatus.toString());}
                    break;
                }
            }
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && 
                    whereeq.fillInEntryValues(valueHoldereq))
            {
                result = super.select(entryToBeUpdate, whereeq, null, null);
            }
            
        }
        return result;
    }
    
    public SqlResultInfo query(Ticket.TicketType ticketType, Integer owner, 
            Integer submitor, Ticket.CiTicketStatus ciStatus, Ticket.CoTicketStatus coStatus)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        
        if(ticketType != null)
        {
            TableEntry entryToBeUpdate = generateTableEntry();
            Map<String, String> valueHolder = Maps.newHashMap();
            
            valueHolder.put("Tickethistory", "selected");
            valueHolder.put("ticketType",  "selected");
            valueHolder.put("requestDate", "selected");          
            valueHolder.put("delvieryFee", "selected");          
            valueHolder.put("totalPrice", "selected");          
            valueHolder.put("unitPrice", "selected");          
            valueHolder.put("Unit", "selected");              
            valueHolder.put("Quantity", "selected");        
            valueHolder.put("rawMaterialName", "selected");   
            valueHolder.put("currentOwnerName", "selected"); 
            valueHolder.put("submitorName", "selected");        
            valueHolder.put("currentOwnerId" , "selected");       
            valueHolder.put("forId", "selected");               
            valueHolder.put("forName", "selected");           
            valueHolder.put("submitorId", "selected");            
            valueHolder.put("status", "selected");                
            valueHolder.put("ticketId", "selected");    
            
            //where
            TableEntry whereeq = generateTableEntry();
            Map<String, String> valueHoldereq = Maps.newHashMap();
            if(owner!=null){valueHoldereq.put("currentOwnerId", owner.toString());} 
            if(submitor!=null){valueHoldereq.put("submitorId", submitor.toString());}
            switch(ticketType)
            {
                case WAREHOUSETICKET_CI:
                {
                    if(ciStatus!=null){valueHoldereq.put("status", ciStatus.toString());}
                    break;
                }
                case WAREHOUSETICKET_CO:
                {
                    if(coStatus!=null){valueHoldereq.put("status", coStatus.toString());}
                    break;
                }
            }
            
            if(entryToBeUpdate.fillInEntryValues(valueHolder) && 
                    whereeq.fillInEntryValues(valueHoldereq))
            {
                result = super.select(entryToBeUpdate, whereeq, null, null);
            }
            
        }
        return result;
    }
    
    public Boolean update(Ticket ticket, Integer ownerId, String ownerName, String status)
    {
        TableEntry entryToBeUpdate = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String newHistory = ticket.getHistory()+ "\n\n--------------------\nTicket updated at "
                +timeFormat.format(new Date(System.currentTimeMillis()));
        
        if(ownerId != null && ownerName != null)
        {
            newHistory += "\n============OwnerUpdated============";
            newHistory += "\nOldOwner: " + ticket.getCurrentOwnerName();
            newHistory += "\nNewOwner: " + ownerName;
            valueHolder.put("currentOwnerId", ownerId.toString());
            valueHolder.put("currentOwnerName", ownerName);
            
        }
        
        if(status != null)
        {
            newHistory += "\n============StatusUpdated============";
            newHistory += "\nOldStatus: " + ticket.getStatus();
            newHistory += "\nNewStatus: " + status;
            valueHolder.put("status", status);
        }
        valueHolder.put("Tickethistory", newHistory);
        entryToBeUpdate.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", ticket.getTicketId().toString());
        wehre_eq.fillInEntryValues(eq);
        
        return super.update(entryToBeUpdate, wehre_eq, null, null).isSucceed();
    }
    
    public Boolean serializeTicketInstance(Ticket t) throws SQLException
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("Tickethistory", "selected");
        valueHolder.put("ticketType",  "selected");
        valueHolder.put("requestDate", "selected");          
        valueHolder.put("delvieryFee", "selected");          
        valueHolder.put("totalPrice", "selected");          
        valueHolder.put("unitPrice", "selected");          
        valueHolder.put("Unit", "selected");              
        valueHolder.put("Quantity", "selected");        
        valueHolder.put("rawMaterialName", "selected");   
        valueHolder.put("currentOwnerName", "selected"); 
        valueHolder.put("submitorName", "selected");        
        valueHolder.put("currentOwnerId" , "selected");       
        valueHolder.put("forId", "selected");            
        valueHolder.put("forName", "selected");             
        valueHolder.put("submitorId", "selected");            
        valueHolder.put("status", "selected");                
        valueHolder.put("ticketId", "selected");    
        entryToBeSelect.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", t.getTicketId().toString());
        wehre_eq.fillInEntryValues(eq);
        
        ResultSet rs = super.select(entryToBeSelect, wehre_eq, null, null).getResultSet();
        if(rs.first())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
            try {
                t.setRequestDate(sdf.parse(rs.getString("requestDate")));
            } catch (ParseException ex) {
                t.setRequestDate(new Date(0));
                Logger.getLogger(CiCoTicketTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            t.setDelvieryFee(Double.parseDouble(rs.getString("delvieryFee")));
            t.setTotalPrice(Double.parseDouble(rs.getString("totalPrice")));      
            t.setUnitPrice(Double.parseDouble(rs.getString("unitPrice")));           
            t.setRawMaterialName(rs.getString("rawMaterialName")); 
            t.setCurrentOwnerName(rs.getString("currentOwnerName"));
            t.setSubmitorName(rs.getString("submitorName"));
            t.setCurrentOwnerId(rs.getInt("currentOwnerId"));
            t.setFor(rs.getInt("forId"));
            t.setForName(rs.getString("forName"));   
            t.setSubmitorId(rs.getInt("submitorId"));
            t.setStatus(rs.getString("status"));
            t.setHistory(rs.getString("Tickethistory"));
            t.setTicketType(rs.getString("ticketType"));
            t.setQuantity(new UnitQuantity(rs.getString("Unit"),Double.parseDouble(rs.getString("Quantity"))));
            result = Boolean.TRUE;
        }
        return result;
    }
    
    static private String EnToCh(String en)
    {
        switch(en)
        {
            case "Tickethistory":{return "历史信息";}          
            case "ticketType":{return "单据类型";} 
            case "requestDate":{return "期望日期";}          
            case "delvieryFee":{return "运输费用";}          
            case "totalPrice":{return "总价（不含运输费用）";}          
            case "unitPrice":{return "单价";}          
            case "Unit":{return "数量单位";}              
            case "Quantity":{return "数量";}        
            case "rawMaterialName":{return "原料名";}   
            case "currentOwnerName":{return "责任人";} 
            case "submitorName":{return "提交人";}        
            case "currentOwnerId" :{return "责任人编号";}       
            case "forId":{return "接收单位编码";}                 
            case "forName":{return "接收单位名称";}           
            case "submitorId":{return "提交人编号";}            
            case "status":{return "状态";}                
            case "ticketId":{return "单据编号";} 
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
    static public String getPrimaryKeyColNameInCh(){return EnToCh("ticketId");} 
    static public String getPrimaryKeyColNameInEng(){return "ticketId";}
    static public String getHistoryColNameInCh(){return EnToCh("name");} 
    static public String getHistoryColNameInEng(){return "name";}
}
