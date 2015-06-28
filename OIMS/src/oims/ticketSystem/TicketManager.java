/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.TicketTable;
import oims.employeeManager.Employee;
import oims.systemManagement.SystemManager;
import oims.ticketSystem.Ticket.TicketType;

/**
 *
 * @author ezouyyi
 */
public class TicketManager implements oims.systemManagement.Client{
    TicketTable itsTicketTable_;
    SystemManager itsSystemManager_;
    TicketFactory itsTicketFactory_;
    
    public TicketManager(SystemManager sm, DataBaseManager dbm)
    {
        itsTicketFactory_ = new TicketFactory(itsTicketTable_);
        itsSystemManager_ = sm;
        itsTicketTable_ = new TicketTable(dbm);
    }
    
    public Map<Integer, Ticket> ListTicket(TicketType tt, Employee owner)
    {
        ResultSet rs = itsTicketTable_.lookupByOwner(tt, owner);
        Map<Integer, Ticket> result = Maps.newHashMap();
        try {
            if(rs.first())
            {
                do
                {
                    Ticket t = itsTicketFactory_.genTicket(tt);
                    itsTicketTable_.serializeTicketInstanceByResultSet(rs, t);
                    result.put(t.getId(), t);
                }while(rs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(TicketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public Ticket ListTicket(TicketType tt, Integer ticketId)
    {
        Ticket result = null;
        ResultSet rs = itsTicketTable_.lookupByTicketId(ticketId);
        try {
            if(rs.first())
            {
                result = itsTicketFactory_.genTicket(tt);
                itsTicketTable_.serializeTicketInstanceByResultSet(rs, result);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TicketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public Ticket createTicket(TicketType tt)
    {
        return itsTicketFactory_.genTicket(tt);
    }

    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status) 
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSystemManager_.statusChangeCompleted(Boolean.TRUE, "ticketM");
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager) {itsSystemManager_ = sysManager;}
}
