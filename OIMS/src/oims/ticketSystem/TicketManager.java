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
import oims.ticketSystem.Ticket.CiTicketStatus;
import oims.ticketSystem.Ticket.TicketType;
import oims.warehouseManagemnet.WareHouseManager;

/**
 *
 * @author ezouyyi
 */
public class TicketManager implements oims.systemManagement.Client{
    CiCoTicketTable itsTicketTable_;
    SystemManager itsSysManager_;
    private DataBaseManager  itsdbm_;
    
    public enum TicketAction
    {
        ACTION_NEXTSTEP,
        ACTION_CANCEL
    }
    
    public TicketManager(DataBaseManager dbm)
    {
        itsdbm_ = dbm;
        itsTicketTable_ = new CiCoTicketTable(dbm);
    }
    
    public SqlResultInfo createTicket(Ticket.TicketType ticketType, Integer reciever, 
            String recieverName, String rawMName, UnitQuantity quantity, String unitPrice,
            String totalPrice, String deliveryFee, Date requestDate)
    {
        Employee submitor = this.itsSysManager_.getCurEmployee();
        return this.itsTicketTable_.NewEntry(ticketType, submitor.getId(), submitor.getName(), 
                reciever, recieverName,rawMName, quantity, unitPrice, totalPrice, deliveryFee, requestDate);
    }

    public SqlDataTable ticketQuery(Ticket.TicketType ticketType, Integer owner, 
            Integer submitor, Ticket.CiTicketStatus ciStatus, Ticket.CoTicketStatus coStatus)
    {
        SqlResultInfo result;
        result = this.itsTicketTable_.query(ticketType, owner, submitor, ciStatus, coStatus);
        SqlDataTable dTable = new SqlDataTable(result.getResultSet(),this.itsTicketTable_.getName());
        this.itsTicketTable_.translateColumnName(dTable.getColumnNames());
        return dTable;
    }
    
    public SqlDataTable ticketQuerySummary(Ticket.TicketType ticketType, Integer owner, 
            Integer submitor, Ticket.CiTicketStatus ciStatus, Ticket.CoTicketStatus coStatus)
    {
        SqlResultInfo result;
        result = this.itsTicketTable_.querySummary(ticketType, owner, submitor, ciStatus, coStatus);
        SqlDataTable dTable = new SqlDataTable(result.getResultSet(),this.itsTicketTable_.getName());
        this.itsTicketTable_.translateColumnName(dTable.getColumnNames());
        return dTable;
    }
        
    public Ticket CiTicketGotoNextStep(Integer ticketId, TicketAction action)
    {
        Ticket ticket = new Ticket(ticketId);
        try {
            this.itsTicketTable_.serializeTicketInstance(ticket);
            if(ticket.getTicketType() == Ticket.TicketType.WAREHOUSETICKET_CI)
            {
                Ticket.CiTicketStatus nextStep = Ticket.CiTicketNextStep(ticket.getCurrentCiStep(), action);
                if(ticket.verifyNextStep(nextStep))
                {
                    switch(ticket.getCurrentCiStep())
                    {
                        case CI_SUBMITTED:
                        {
                            if(nextStep == Ticket.CiTicketStatus.CI_PAYED)
                            {
                                // CI ticket: for is warehouse ID, need to find keeper
                                Integer warehouseId = ticket.getFor();
                                WareHouseManager whm = 
                                        (WareHouseManager)this.itsSysManager_.getClient(SystemManager.clientType.WAREHOUSE_MANAGER);
                                Integer nextOwnerId = whm.getKeeperId(warehouseId);
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                String nextOwnerName = eyM.getEmployeeName(nextOwnerId);
                                this.itsTicketTable_.update(ticket, nextOwnerId, nextOwnerName, nextStep.toString());
                            }
                            else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            break;
                        }
                        case CI_PAYED:
                        {
                            if(nextStep == Ticket.CiTicketStatus.CI_CHECKEDIN)
                            {
                                //TODO: should go to financial responser set to nobody for now
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            break;
                        }
                        case CI_CHECKEDIN:
                        {
                            if(nextStep == Ticket.CiTicketStatus.CI_CLOSE)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            else if(nextStep == Ticket.CiTicketStatus.CI_CANCLED)
                            {
                                // NOT ALLOWED Financial should check this ticket
                                // and close it. 
                            }
                            break;
                        }
                        default:
                        {
                            //do nothing
                        }
                    }

                    // reload ticket content
                    this.itsTicketTable_.serializeTicketInstance(ticket);
                }
            }
        } catch (SQLException ex) {
            ticket = null;
            Logger.getLogger(TicketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return ticket;
    }
    
    public Ticket CoTicketGotoNextStep(Integer ticketId, TicketAction action)
    {
        Ticket ticket = new Ticket(ticketId);
        try {
            this.itsTicketTable_.serializeTicketInstance(ticket);
            if(ticket.getTicketType() == Ticket.TicketType.WAREHOUSETICKET_CO)
            {
                Ticket.CoTicketStatus nextStep = Ticket.CoTicketNextStep(ticket.getCurrentCoStep(), action);
                if(ticket.verifyNextStep(nextStep))
                {
                    switch(ticket.getCurrentCoStep())
                    {
                        case CO_SUMITTED:
                        {
                            if(nextStep == Ticket.CoTicketStatus.CO_REQUESTSENT)
                            {
                                // CO ticket: for is warehouse ID, need to find keeper
                                Integer warehouseId = ticket.getFor();
                                WareHouseManager whm = 
                                        (WareHouseManager)this.itsSysManager_.getClient(SystemManager.clientType.WAREHOUSE_MANAGER);
                                Integer nextOwnerId = whm.getKeeperId(warehouseId);
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                String nextOwnerName = eyM.getEmployeeName(nextOwnerId);
                                this.itsTicketTable_.update(ticket, nextOwnerId, nextOwnerName, nextStep.toString());
                            }
                            else if(nextStep == Ticket.CoTicketStatus.CO_CANCLED)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            break;
                        }
                        case CO_REQUESTSENT:
                        {
                            if(nextStep == Ticket.CoTicketStatus.CO_CHECKEDOUT)
                            {
                                this.itsTicketTable_.update(ticket, ticket.getSubmitorId(), ticket.getSubmitorName(), nextStep.toString());
                            }
                            else if(nextStep == Ticket.CoTicketStatus.CO_CANCLED)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            break;
                        }
                        case CO_CHECKEDOUT:
                        {
                            if(nextStep == Ticket.CoTicketStatus.CO_CLOSE || 
                                    nextStep == Ticket.CoTicketStatus.CO_CANCLED)
                            {
                                EmployeeManager eyM = 
                                        (EmployeeManager)this.itsSysManager_.getClient(SystemManager.clientType.EMPLOYEE_MANAGER);
                                Integer employeeId = eyM.getInvalidEmployeeId();
                                this.itsTicketTable_.update(ticket, employeeId, "noName", nextStep.toString());
                            }
                            break;
                        }
                        default:
                        {
                            //do nothing
                        }
                    }

                    // reload ticket content
                    this.itsTicketTable_.serializeTicketInstance(ticket);
                }
            }
        } catch (SQLException ex) {
            ticket = null;
            Logger.getLogger(TicketManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ticket;
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status) 
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
            case SYS_REGISTER:
            {
                this.itsdbm_.registerTable(itsTicketTable_);
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager) {itsSysManager_ = sysManager;}
}
