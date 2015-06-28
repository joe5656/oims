/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.tables.TicketTable;
import oims.employeeManager.Employee;

/**
 *
 * @author ezouyyi
 */
public class Ticket {
    protected Integer ticketId_;
    protected String  ticketName_;
    protected TicketType ticketType_;
    protected String     specifiedArea_;
    protected Integer    ownerId_;
    protected String     ownerName_;
    protected Integer    submitorId_;
    protected String     submitorName_;
    protected Boolean    syncd_;
    protected TicketTable itsTable_;
    protected Boolean    needToUpdate_;
    protected String     history_;
    
    public Ticket(TicketTable table, TicketType tt)
    {
        needToUpdate_ = Boolean.FALSE;
        syncd_ = Boolean.FALSE;
        itsTable_ = table;
    }
    
    public enum TicketType
    {
        WAREHOUSETICKET_CI,
        WAREHOUSETICKET_CO
    }
    
    public Integer getId(){return ticketId_;}
    public String  getTicketName(){return ticketName_;}
    public TicketType getTicketType(){return ticketType_;}
    public Integer getOwnerId(){return ownerId_;}
    public String  getOwnerName(){return ownerName_; }
    public Integer getSubmitorId(){return submitorId_;}
    public String  getSubmitorName(){return submitorName_;}
    public Boolean isSyncd(){return syncd_;}
    public String  getSpecifiedContent(){return specifiedArea_;}
    public Boolean hasNewData(){return needToUpdate_;}
    public String  getHist(){return history_;}
    
    public void  setId(Integer ticketId){ ticketId_ = ticketId;}
    public void  setTicketName(String ticketName){ ticketName_ = ticketName;}
    public void  setTicketType(String ticketType)
    { 
        switch(ticketType)
        {
            case "WAREHOUSETICKET_CI":
            {
                ticketType_ = TicketType.WAREHOUSETICKET_CI;
                break;
            }
            case "WAREHOUSETICKET_CO":
            {
                ticketType_ = TicketType.WAREHOUSETICKET_CO;
                break;
            }
        }
    }
    public void  setOwnerId(Integer ownerId){ ownerId_ = ownerId;}
    public void  setOwnerName(String ownerName){ ownerName_ = ownerName; }
    public void  setSubmitorId(Integer submitorId){ submitorId_ = submitorId;}
    public void  setSubmitorName(String submitorName){ submitorName_ = submitorName;}
    public void  setSyncd(Boolean syncd){ syncd_ = syncd;}
    public void  setSpecifiedContent(String specifiedArea){specifiedArea_ = specifiedArea;}
    public void  setUpdated(){needToUpdate_ = Boolean.TRUE;}
    public void  clearUpdated(){needToUpdate_ = Boolean.FALSE;}
    public void  setHistory(String hist){history_ = hist;}
    
    public void serilze()
    {
        try {
            itsTable_.serializeTicketInstance(this);
        } catch (SQLException ex) {
            Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
    protected void addHistory(String histContent)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = timeFormat.format(new Date(System.currentTimeMillis()));
        history_ += "\n " + time + " " + histContent;
    }
    
    /*Need to be overrided*/
    public Boolean moveToNextStatus(Employee employee){return Boolean.FALSE;};
    
    /*Need to be overrided*/
    public Boolean explain(){return Boolean.FALSE;}
    
    /*Need to be overrided*/
    public String getStatus(){return "status unknown";}
    
    /*Need to be overrided*/
    public void setStatus(String st){}
    
    /*Need to be overrided*/
    public Boolean upload(){return Boolean.FALSE;}
    
    protected Boolean uploadToDb(String status)
    {
        return itsTable_.update(ticketId_, ownerId_, ownerName_, specifiedArea_, history_, status);
    }
}
