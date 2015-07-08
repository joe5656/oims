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
import oims.dataBase.tables.CiCoTicketTable;
import oims.employeeManager.Employee;
import oims.support.util.UnitQuantity;

/**
 *
 * @author ezouyyi
 */
public class Ticket {
    private TicketType ticketType_;   
    private UnitQuantity  Quantity_; 
    private Date    requestDate_;          
    private Double  delvieryFee_;          
    private Double  totalPrice_;          
    private Double  unitPrice_;             
    private String  rawMaterialName_;   
    private String  currentOwnerName_; 
    private String  submitorName_;        
    private Integer currentOwnerId_;       
    private Integer for_;     
    private String  forName_;
    private Integer rawMaterialId_;         
    private Integer submitorId_;            
    private String  status_;                
    private Integer ticketId_; 
    private String  history_;
        
    public Ticket(TicketType tt)
    {
        ticketType_ = tt;
    }
    
    public Ticket(TicketType tt, Integer ticketId)
    {
        ticketType_ = tt;
        ticketId_ = ticketId;
    }
    
    public Ticket(Integer ticketId)
    {
        ticketId_ = ticketId;
    }
        
    public Boolean verifyNextStep(CiTicketStatus nextStep)
    {
        Boolean canGo = Boolean.FALSE;
        if(this.ticketType_ != TicketType.WAREHOUSETICKET_CI)
        {
            return canGo;
        }
        
        canGo = true;
        
        return canGo;
    }
    
    public Boolean verifyNextStep(CoTicketStatus nextStep)
    {
        Boolean canGo = Boolean.FALSE;
        if(this.ticketType_ != TicketType.WAREHOUSETICKET_CO)
        {
            return canGo;
        }
        
        canGo = true;
        
        return canGo;
    }
    
    public CiTicketStatus getCurrentCiStep()
    {
        CiTicketStatus status = CiTicketStatus.CI_NONE;
        if(this.ticketType_ == TicketType.WAREHOUSETICKET_CI)
        {
            switch(status_)
            {
                case "CI_NONE":{status = CiTicketStatus.CI_NONE;break;}
                case "CI_SUBMITTED":{status = CiTicketStatus.CI_SUBMITTED;break;}
                case "CI_PAYED":{status = CiTicketStatus.CI_PAYED;break;}
                case "CI_CHECKEDIN":{status = CiTicketStatus.CI_CHECKEDIN;break;}
                case "CI_CLOSE":{status = CiTicketStatus.CI_CLOSE;break;}
                case "CI_CANCLED":{status = CiTicketStatus.CI_CANCLED;break;}
            }
        }
        return status;
    }
    
    public CoTicketStatus getCurrentCoStep()
    {
        CoTicketStatus status = CoTicketStatus.CO_NONE;
        if(this.ticketType_ == TicketType.WAREHOUSETICKET_CO)
        {
            switch(status_)
            {
                case "CO_NONE":{status = CoTicketStatus.CO_NONE;break;}
                case "CO_SUMITTED":{status = CoTicketStatus.CO_SUMITTED;break;}
                case "CO_CHECKEDOUT":{status = CoTicketStatus.CO_CHECKEDOUT;break;}
                case "CO_CLOSE":{status = CoTicketStatus.CO_CLOSE;break;}
                case "CO_REQUESTSENT":{status = CoTicketStatus.CO_REQUESTSENT;break;}
            }
        }
        return status;
    }
    
    public enum TicketType
    {
        WAREHOUSETICKET_NONE,
        WAREHOUSETICKET_CI,
        WAREHOUSETICKET_CO
    }
    
    public enum CiTicketStatus
    {
        CI_NONE,
        CI_SUBMITTED,
        CI_PAYED,
        CI_CHECKEDIN,
        CI_CLOSE,
        CI_CANCLED
    }
    
    public enum CoTicketStatus
    {
        CO_NONE,
        CO_SUMITTED,
        CO_REQUESTSENT,
        CO_CHECKEDOUT,
        CO_CLOSE,
        CO_CANCLED
    }
    
    public Date    getRequestDate(){return requestDate_;}          
    public Double  getDelvieryFee(){return delvieryFee_;}          
    public Double  getTotalPrice(){return totalPrice_;}          
    public Double  getUnitPrice(){return unitPrice_;}             
    public String  getRawMaterialName(){return rawMaterialName_;}   
    public String  getCurrentOwnerName(){return currentOwnerName_;} 
    public String  getSubmitorName(){return submitorName_;}        
    public Integer getCurrentOwnerId(){return currentOwnerId_;}       
    public Integer getFor(){return for_;}                      
    public String  getForName(){ return forName_;}               
    public Integer getRawMaterialId(){return rawMaterialId_;}         
    public Integer getSubmitorId(){return submitorId_;}            
    public String  getStatus(){return status_;}                
    public Integer getTicketId(){return ticketId_;} 
    public String  getHistory(){return history_;}
    public TicketType getTicketType(){return ticketType_;}
    public UnitQuantity getQuantity(){return Quantity_;}
    
    public void setRequestDate(Date requestDate){  requestDate_ = requestDate;}          
    public void setDelvieryFee(Double delvieryFee){  delvieryFee_ = delvieryFee;}          
    public void setTotalPrice(Double totalPrice){  totalPrice_ = totalPrice;}          
    public void setUnitPrice(Double unitPrice){  unitPrice_ = unitPrice;}             
    public void setRawMaterialName(String rawMaterialName){  rawMaterialName_ = rawMaterialName;}   
    public void setCurrentOwnerName(String currentOwnerName){  currentOwnerName_ = currentOwnerName;} 
    public void setSubmitorName(String submitorName){  submitorName_ = submitorName;}        
    public void setCurrentOwnerId(Integer currentOwnerId){  currentOwnerId_ = currentOwnerId;}       
    public void setFor(Integer forId){  for_ = forId;}                       
    public void setForName(String name){  forName_ = name;}                   
    public void setRawMaterialId(Integer rawMaterialId){  rawMaterialId_ = rawMaterialId;}         
    public void setSubmitorId(Integer submitorId){  submitorId_ = submitorId;}            
    public void setStatus(String status){  status_ = status;}                
    public void setTicketId(Integer ticketId){  ticketId_ = ticketId;} 
    public void setHistory(String history){  history_ = history;}
    public void setQuantity(UnitQuantity Quantity){  Quantity_ = Quantity;}
    public void setTicketType(String type)
    {
        switch(type)
        {
            case "WAREHOUSETICKET_CI":
            {
                this.ticketType_ = TicketType.WAREHOUSETICKET_CI;
                break;
            }
            case "WAREHOUSETICKET_CO":
            {
                this.ticketType_ = TicketType.WAREHOUSETICKET_CO;
                break;
            }
            default:
            {
                this.ticketType_ = TicketType.WAREHOUSETICKET_NONE;
                break;
            }
        }
    }
    static public Ticket.CiTicketStatus CiTicketNextStep(Ticket.CiTicketStatus curStep, TicketManager.TicketAction action)
    {
        Ticket.CiTicketStatus nextStep;
        switch(action)
        {
            case ACTION_NEXTSTEP:
            {
                switch(curStep)
                {
                    case CI_NONE:{nextStep = CiTicketStatus.CI_NONE;break;}
                    case CI_SUBMITTED:{nextStep = CiTicketStatus.CI_PAYED;break;}
                    case CI_PAYED:{nextStep = CiTicketStatus.CI_CHECKEDIN;break;}
                    case CI_CHECKEDIN:{nextStep = CiTicketStatus.CI_CLOSE;break;}
                    case CI_CLOSE:{nextStep = CiTicketStatus.CI_CLOSE;break;}
                    case CI_CANCLED:{nextStep = CiTicketStatus.CI_CANCLED;break;}
                    default:{nextStep = CiTicketStatus.CI_NONE;break;}
                }
                break;
            }
            case ACTION_CANCEL:
            {
                switch(curStep)
                {
                    case CI_NONE:{nextStep = CiTicketStatus.CI_NONE;break;}
                    case CI_SUBMITTED:{nextStep = CiTicketStatus.CI_CANCLED;break;}
                    case CI_PAYED:{nextStep = CiTicketStatus.CI_CANCLED;break;}
                    case CI_CHECKEDIN:{nextStep = CiTicketStatus.CI_CHECKEDIN;break;}
                    case CI_CLOSE:{nextStep = CiTicketStatus.CI_CLOSE;break;}
                    case CI_CANCLED:{nextStep = CiTicketStatus.CI_CANCLED;break;}
                    default:{nextStep = CiTicketStatus.CI_NONE;break;}
                }
                break;
            }
            default:
            {
                nextStep = curStep;
                break;
            }
        }
        return nextStep;
    }
    
    static public Ticket.CoTicketStatus CoTicketNextStep(Ticket.CoTicketStatus curStep, TicketManager.TicketAction action)
    {
        Ticket.CoTicketStatus nextStep;
        switch(action)
        {
            case ACTION_NEXTSTEP:
            {
                switch(curStep)
                {
                    case CO_NONE:{nextStep = CoTicketStatus.CO_NONE;break;}
                    case CO_SUMITTED:{nextStep = CoTicketStatus.CO_REQUESTSENT;break;}
                    case CO_REQUESTSENT:{nextStep = CoTicketStatus.CO_CHECKEDOUT;break;}
                    case CO_CHECKEDOUT:{nextStep = CoTicketStatus.CO_CLOSE;break;}
                    case CO_CANCLED:{nextStep = CoTicketStatus.CO_CANCLED;break;}
                    case CO_CLOSE:{nextStep = CoTicketStatus.CO_CLOSE;break;}
                    default:{nextStep = CoTicketStatus.CO_NONE;break;}
                }
                break;
            }
            case ACTION_CANCEL:
            {
                switch(curStep)
                {
                    case CO_NONE:{nextStep = CoTicketStatus.CO_NONE;break;}
                    case CO_SUMITTED:{nextStep = CoTicketStatus.CO_CANCLED;break;}
                    case CO_REQUESTSENT:{nextStep = CoTicketStatus.CO_CANCLED;break;}
                    case CO_CHECKEDOUT:{nextStep = CoTicketStatus.CO_CANCLED;break;}
                    case CO_CLOSE:{nextStep = CoTicketStatus.CO_CLOSE;break;}
                    default:{nextStep = CoTicketStatus.CO_NONE;break;}
                }
                break;
            }
            default:
            {
                nextStep = curStep;
                break;
            }
        }
        return nextStep;
    }
}
