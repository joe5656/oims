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
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.employeeManager.Employee;
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
        super.registerColumn("Tickethistory", Db_publicColumnAttribute.ATTRIBUTE_NAME.FLOAT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
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
        super.registerColumn("for", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("rawMaterialId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("status", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }   
    

    public SqlResultInfo NewEntry(Ticket.TicketType ticketType, Integer submitorId, String submitorName,
            Integer reciever, Integer rawMId, UnitQuantity quantity, Double unitPrice,
            Double totalPrice, Double deliveryFee, Date requestDate)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        TableEntry entryToBeInsert = generateTableEntry();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String historyString = "\nTicket submitted at "+timeFormat.format(new Date(System.currentTimeMillis()));
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketType", ticketType.toString());
        valueHolder.put("submitorId", submitorId.toString());
        valueHolder.put("submitorName", submitorName);
        valueHolder.put("rawMaterialId", rawMId.toString());
        valueHolder.put("Quantity", quantity.getQuantity().toString());
        valueHolder.put("Unit", quantity.getUnit().getUnitName());
        valueHolder.put("for", reciever.toString());
        valueHolder.put("requestDate", timeFormat.format(requestDate));
        valueHolder.put("delvieryFee", deliveryFee.toString());
        valueHolder.put("currentOwnerId", submitorId.toString());
        valueHolder.put("currentOwnerName", submitorName);
        valueHolder.put("Tickethistory", historyString);
        if(ticketType == TicketType.WAREHOUSETICKET_CI)
        {
            valueHolder.put("unitPrice", unitPrice.toString());
            valueHolder.put("totalPrice", totalPrice.toString());
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
    
    public void ticketNextStep()
    {
        
    }
    
    public SqlResultInfo query()
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        return result;
    }
    
    public Boolean update(Ticket ticket, Integer ownerId, String ownerName, String status)
    {
        TableEntry entryToBeUpdate = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String newHistory = ticket.getHist() + "\n\n--------------------\nTicket updated at "
                +timeFormat.format(new Date(System.currentTimeMillis()));
        
        if(ownerId != null && ownerName != null)
        {
            newHistory += "\n============OwnerUpdated============";
            newHistory += "\nOldOwner: " + ticket.getOwnerName();
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
        valueHolder.put("ticketHistory", newHistory);
        entryToBeUpdate.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", ticket.getId().toString());
        wehre_eq.fillInEntryValues(eq);
        
        return super.update(entryToBeUpdate, wehre_eq, null, null).isSucceed();
    }
    
    public void serializeTicketInstanceByResultSet(ResultSet rs, Ticket t) throws SQLException
    {
        t.setId(rs.getInt("ticketId"));
        t.setOwnerId(rs.getInt("currentOwnerId"));
        t.setOwnerName(rs.getString("currentOwnerName"));
        t.setSpecifiedContent(rs.getString("ticketSpecifiedContent"));
        t.explain();
        t.setStatus(rs.getString("status"));
        t.setSubmitorId(rs.getInt("submitorId"));
        t.setSubmitorName(rs.getString("submitorName"));
        t.setTicketTitle(rs.getString("tickettitle"));
        t.setTicketType(rs.getString("ticketType"));
        t.setSyncd(Boolean.TRUE);
    }
    
    public Boolean serializeTicketInstance(Ticket t) throws SQLException
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketId", "select");
        valueHolder.put("tickettitle", "select");
        valueHolder.put("status", "select");
        valueHolder.put("submitorId", "select");
        valueHolder.put("ticketType", "select");
        valueHolder.put("currentOwnerId", "select");
        valueHolder.put("submitorName", "select");
        valueHolder.put("currentOwnerName", "select");
        valueHolder.put("ticketSpecifiedContent", "select");
        valueHolder.put("ticketHistory", "select");
        entryToBeSelect.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", t.getId().toString());
        wehre_eq.fillInEntryValues(eq);
        
        ResultSet rs = super.select(entryToBeSelect, wehre_eq, null, null).getResultSet();
        if(rs.first())
        {
            t.setId(rs.getInt("ticketId"));
            t.setOwnerId(rs.getInt("currentOwnerId"));
            t.setOwnerName(rs.getString("currentOwnerName"));
            t.setSpecifiedContent(rs.getString("ticketSpecifiedContent"));
            t.setStatus(rs.getString("status"));
            t.setSubmitorId(rs.getInt("submitorId"));
            t.setSubmitorName(rs.getString("submitorName"));
            t.setTicketTitle(rs.getString("tickettitle"));
            t.setTicketType(rs.getString("ticketType"));
            t.setHistory(rs.getString("ticketHistory"));
            t.setSyncd(Boolean.TRUE);
            result = Boolean.TRUE;
        }
        return result;
    }
    
    public ResultSet lookupByOwner(TicketType tt, Employee owner)
    {
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketId", "select");
        valueHolder.put("tickettitle", "select");
        valueHolder.put("status", "select");
        valueHolder.put("submitorId", "select");
        valueHolder.put("ticketType", "select");
        valueHolder.put("currentOwnerId", "select");
        valueHolder.put("submitorName", "select");
        valueHolder.put("currentOwnerName", "select");
        valueHolder.put("ticketSpecifiedContent", "select");
        valueHolder.put("ticketHistory", "select");
        entryToBeSelect.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketType", tt.toString());
        eq.put("currentOwnerId", owner.getId().toString());
        eq.put("currentOwnerName", owner.getName());
        wehre_eq.fillInEntryValues(eq);
        
        return super.select(entryToBeSelect, wehre_eq, null, null).getResultSet();
    }
    
    public ResultSet lookupByTicketId(Integer ticketId)
    {
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketId", "select");
        valueHolder.put("tickettitle", "select");
        valueHolder.put("status", "select");
        valueHolder.put("submitorId", "select");
        valueHolder.put("ticketType", "select");
        valueHolder.put("currentOwnerId", "select");
        valueHolder.put("submitorName", "select");
        valueHolder.put("currentOwnerName", "select");
        valueHolder.put("ticketSpecifiedContent", "select");
        valueHolder.put("ticketHistory", "select");
        entryToBeSelect.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", ticketId.toString());
        wehre_eq.fillInEntryValues(eq);
        
        return super.select(entryToBeSelect, wehre_eq, null, null).getResultSet();
    }
}
