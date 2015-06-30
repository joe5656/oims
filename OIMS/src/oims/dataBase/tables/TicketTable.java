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
import oims.ticketSystem.Ticket;
import oims.ticketSystem.Ticket.TicketType;

/**
 *
 * @author ezouyyi
 */
public class TicketTable extends Db_table {
    static public String getDerivedTableName()
    {
        return "TicketTable";
    }
    public TicketTable(DataBaseManager dbm)
    {
        super("TicketTable", dbm, Table_Type.TABLE_TYPE_MIRROR);
        super.registerColumn("ticketHistory", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketSpecifiedContent", Db_publicColumnAttribute.ATTRIBUTE_NAME.TEXT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("currentOwnerName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("currentOwnerId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketType", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("submitorId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER,  Boolean.FALSE,   Boolean.FALSE,  Boolean.FALSE, null);
        super.registerColumn("status", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketName", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("ticketId", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }   
    

    public Boolean NewEntry(String ticketName, String ticketType,
            String currentOwnerName, String submitorName, String ticketSpecifiedContent, 
            Integer submitor, Integer currentOwnerId, String ticketHistory, String status)
    {
        Boolean result = Boolean.FALSE;

        TableEntry entryToBeInsert = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketName", ticketName);
        valueHolder.put("ticketType", ticketType);
        valueHolder.put("submitorId", submitor.toString());
        valueHolder.put("currentOwnerId", currentOwnerId.toString());
        valueHolder.put("currentOwnerName", currentOwnerName);
        valueHolder.put("ticketSpecifiedContent", ticketSpecifiedContent);
        valueHolder.put("submitorName", submitorName);
        valueHolder.put("ticketHistory", ticketHistory);
        valueHolder.put("status", status);
        if(entryToBeInsert.fillInEntryValues(valueHolder))
        {
            if(super.insertRecord(entryToBeInsert).isSucceed())
            {
                result = Boolean.TRUE;
            }
        }
        
        return result;
    }    
    
    public Boolean update(Integer ticketId, Integer currentOwnerId, String currentOwnerName, 
            String ticketSpecifiedContent, String ticketHistory, String status)
    {
        TableEntry entryToBeUpdate = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("currentOwnerId", currentOwnerId.toString());
        valueHolder.put("currentOwnerName", currentOwnerName);
        valueHolder.put("ticketSpecifiedContent", ticketSpecifiedContent);
        valueHolder.put("ticketHistory", ticketHistory);
        valueHolder.put("status", status);
        entryToBeUpdate.fillInEntryValues(valueHolder);
        
        // where 
        TableEntry wehre_eq = generateTableEntry();
        Map<String, String> eq = Maps.newHashMap();
        eq.put("ticketId", ticketId.toString());
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
        t.setTicketName(rs.getString("ticketName"));
        t.setTicketType(rs.getString("ticketType"));
        t.setSyncd(Boolean.TRUE);
    }
    
    public Boolean serializeTicketInstance(Ticket t) throws SQLException
    {
        Boolean result = Boolean.FALSE;
        TableEntry entryToBeSelect = generateTableEntry();
        Map<String, String> valueHolder = Maps.newHashMap();
        valueHolder.put("ticketId", "select");
        valueHolder.put("ticketName", "select");
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
            t.setTicketName(rs.getString("ticketName"));
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
        valueHolder.put("ticketName", "select");
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
        valueHolder.put("ticketName", "select");
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
