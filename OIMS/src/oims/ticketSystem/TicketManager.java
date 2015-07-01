/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.CiCoTicketTable;
import oims.employeeManager.Employee;
import oims.employeeManager.EmployeeManager;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.support.util.UnitQuantity;
import oims.systemManagement.SystemManager;
import oims.ticketSystem.Ticket.TicketType;
import oims.warehouseManagemnet.WareHouseManager;

/**
 *
 * @author ezouyyi
 */
public class TicketManager implements oims.systemManagement.Client{
    CiCoTicketTable itsTicketTable_;
    SystemManager itsSystemManager_;
    TicketFactory itsTicketFactory_;
    
    public enum TicketAction
    {
        ACTION_NEXTSTEP,
        ACTION_CANCEL
    }
    
    public TicketManager(DataBaseManager dbm)
    {
        itsTicketFactory_ = new TicketFactory(itsTicketTable_);
        itsTicketTable_ = new CiCoTicketTable(dbm);
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
    
    public SqlResultInfo createTicket(Ticket.TicketType ticketType, Integer submitorId, String submitorName,
            Integer reciever, Integer rawMId, UnitQuantity quantity, double unitPrice,
            double totalPrice, double deliveryFee, Date requestDate)
    {
        return this.itsTicketTable_.NewEntry(ticketType, submitorId, submitorName, 
                reciever, rawMId, quantity, unitPrice, totalPrice, deliveryFee, requestDate);
    }

    public SqlDataTable ticketQuery(Ticket.TicketType ticketType, Integer owner, 
            Integer submitor, Boolean queryBySubmitor, Boolean queryByOwner, 
            Ticket.CiTicketStatus ciStatus, Ticket.CoTicketStatus coStatus)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        result = this.itsTicketTable_.query();
        SqlDataTable dTable = new SqlDataTable(result.getResultSet(),this.itsTicketTable_.getName());
        return dTable;
    }
    
    public Ticket CiTicketGotoNextStep(Integer ticketId, TicketAction action)
    {
        Ticket ticket = new Ticket(Ticket.TicketType.WAREHOUSETICKET_CI, ticketId);
        try {
            this.itsTicketTable_.serializeTicketInstance(ticket);
            Ticket.CiTicketStatus nextStep = CiTicketNextStep(ticket, action);
            if(ticket.verifyNextStep(nextStep))
            {
                switch(ticket.getCurrentCiStep())
                {
                    case CI_SUBMITTED:
                    {
                        if(nextStep == Ticket.CiTicketStatus.CI_PAYED)
                        {
                            // CI ticket: for is warehouse ID, need to find keeper
                            Integer warehouseId = ticket.getfor();
                            WareHouseManager whm = 
                                    (WareHouseManager)this.itsSystemManager_.getClient(SystemManager.clientType.WAREHOUSE_MANAGER);
                            Integer nextOwnerId = whm.getKeeperId(warehouseId);
                            EmployeeManager eyM = 
                                    (EmployeeManager)this.itsSystemManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                            String nextOwnerName = eyM.getEmployeeName(nextOwnerId);
                            this.itsTicketTable_.update(ticket, nextOwnerId, nextOwnerName, nextStep.toString());
                        }
                        else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                        {
                            
                        }
                        break;
                    }
                    case CI_PAYED:
                    {
                        if(nextStep == Ticket.CiTicketStatus.CI_PAYED)
                        {
                            
                        }
                        else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                        break;
                    }
                    case CI_CHECKEDIN:
                    {
                        if(nextStep == Ticket.CiTicketStatus.CI_PAYED)
                        {
                            
                        }
                        else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                        break;
                    }
                    case CI_CLOSE:
                    {
                        if(nextStep == Ticket.CiTicketStatus.CI_PAYED)
                        {
                            
                        }
                        else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                        break;
                    }
                    case CI_CANCLED:
                    {
                        break;
                    } 
                }
            }
        } catch (SQLException ex) {
            ticket = null;
            Logger.getLogger(TicketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ticket;
    }
    
    private Ticket.CiTicketStatus CiTicketNextStep(Ticket ticket, TicketAction action)
    {
        Ticket.CiTicketStatus nextStep;
        switch(action)
        {
            case ACTION_NEXTSTEP:
            {
                break;
            }
            case ACTION_CANCEL:
            {
                break;
            }
            default:
            {
                break;
            }
        }
        return nextStep;
        
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
