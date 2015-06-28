/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem.Tickets;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import oims.dataBase.tables.TicketTable;
import oims.employeeManager.Employee;
import static oims.ticketSystem.Ticket.TicketType;
import oims.support.util.UnitQuantity;
import oims.ticketSystem.Ticket;
/**
 *
 * @author ezouyyi
 */
public class WareHouseCiTicket extends Ticket {

    // Map<rawMaterialId, <UnitQuantity,checkedIn>>
    Map<Integer, groupedRMInfo>  rawMaterialList_;
    Status      st_;
    
    enum Status
    {
        SUBMITTED,
        APPROVED,
        ORDERED_AND_WAITING_FOR_ARRIVAL,
        CLOSE
    }
    
    private class groupedRMInfo
    {
        Boolean checkedIn_;
        UnitQuantity quantity_;
        String  rawMaterialName_;
        
        groupedRMInfo(UnitQuantity quantity, Boolean checkedIn, String name)
        {
            quantity_ = quantity;
            checkedIn_ = checkedIn;
            rawMaterialName_ = name;
        }
        
        public void setCheckedIn(Boolean checked)
        {
            checkedIn_ = checked;
        }
        
        public String getName(){return rawMaterialName_;}
        public UnitQuantity getQuantity(){return quantity_;}
        public Boolean isCheckedIn(){return checkedIn_;}
    }
    
    public WareHouseCiTicket(TicketTable table)
    {
        super(table, TicketType.WAREHOUSETICKET_CI);
        rawMaterialList_ = Maps.newHashMap();
    }
    
    private String ChineseSt(Status st)
    {
        String result = "";
        switch(st_)
        {
            case SUBMITTED:
            {
                result = "待审核";
                break;
            }
            case APPROVED:
            {
                result = "审核通过，待下单";
                break;
            }
            case ORDERED_AND_WAITING_FOR_ARRIVAL:
            {
                result = "已下单，待入库";
                break;
            }
            case CLOSE:
            {
                result = "关闭";
                break;
            }
        }
        return result;
    }
    
    @Override
    public Boolean moveToNextStatus(Employee nextOwner)
    {
        Boolean canMove = Boolean.FALSE;
        switch(st_)
        {
            case SUBMITTED:
            {
                canMove = Boolean.TRUE;
                st_ = Status.APPROVED;
                super.setOwnerId(nextOwner.getId());
                super.setOwnerName(nextOwner.getName());
                setUpdated();
                break;
            }
            case APPROVED:
            {
                canMove = Boolean.TRUE;
                st_ = Status.ORDERED_AND_WAITING_FOR_ARRIVAL;
                super.setOwnerId(nextOwner.getId());
                super.setOwnerName(nextOwner.getName());
                setUpdated();
                break;
            }
            case ORDERED_AND_WAITING_FOR_ARRIVAL:
            {
                if(allCheckedIn())
                {
                    canMove = Boolean.TRUE;
                    st_ = Status.CLOSE;
                    super.setOwnerId(nextOwner.getId());
                    super.setOwnerName(nextOwner.getName());
                    setUpdated();
                    break;
                }
            }
        }
        super.addHistory("单据状态改变到： "+ChineseSt(st_));
        return canMove;
    };
    
    public Boolean allCheckedIn()
    {
        Boolean returnValue = Boolean.TRUE;
        Set<Entry<Integer, groupedRMInfo>> entrySet = rawMaterialList_.entrySet();
        Iterator<Entry<Integer, groupedRMInfo>> itr = entrySet.iterator();
        while(itr.hasNext())
        {
            Entry<Integer, groupedRMInfo> tmpEntry = itr.next();
            if(!tmpEntry.getValue().isCheckedIn())
            {
                returnValue = Boolean.FALSE;
                break;
            }
        }
        return returnValue;
        
    }
    
    /**
     * create formated string for specified area
     * format: rawMaterialId(1):unit:quantity:IscheckedIn(0,1):rawMaterialName(1)
     *         |....
     *         |rawMaterialId(N):unit:quantityIscheckedIn(0,1):rawMaterialName(N)
     * @return 
     */
    private String subSerilze()
    {
        Set<Entry<Integer, groupedRMInfo>> entrySet = rawMaterialList_.entrySet();
        Iterator<Entry<Integer, groupedRMInfo>> itr = entrySet.iterator();
        String returnString = "";
        while(itr.hasNext())
        {
            Entry<Integer, groupedRMInfo> tmpEntry = itr.next();
            if(!"".equals(returnString))
            {
                returnString += "|";
            }
            returnString += tmpEntry.getValue().toString() + ":" 
                    + tmpEntry.getValue().getQuantity().getUnit().getUnitName() + ":"
                    + tmpEntry.getValue().getQuantity().getQuantity().toString() + ":"
                    + (tmpEntry.getValue().isCheckedIn()?"1":"0"+":"+tmpEntry.getValue().getName());
            
        }
        return returnString;
    };
    
    @Override
    public Boolean explain()
    {
        Boolean returnValue = Boolean.FALSE;
        if(specifiedArea_ != null)
        {
            String[] rMList = specifiedArea_.split("|");
            for(Integer i = 0; i < rMList.length; i++)
            {
                String[] details = rMList[i].split(":");
                rawMaterialList_.put(Integer.parseInt(details[0]), 
                        new groupedRMInfo(new UnitQuantity(details[1], Double.parseDouble(details[2])), Integer.parseInt(details[3])==1, details[4]));
            }
            returnValue = Boolean.TRUE;
        }
        return returnValue;
    }
    
    public void setCheckedIn(Integer rMId)
    {
        if(rawMaterialList_ != null)
        {
            // need to be update the DB at once and refresh the specifiedArea_
            rawMaterialList_.get(rMId).setCheckedIn(Boolean.TRUE);
            specifiedArea_ = subSerilze();
            super.addHistory("单据状态改变到： "+ChineseSt(st_));
            setUpdated();
        }
    }
    
    @Override
    public String getStatus()
    {
        return st_.toString();
    }
    
    @Override
    public void setStatus(String st)
    {
        switch(st)
        {
            case "SUBMITTED":
            {
                st_ = Status.SUBMITTED;
                break;
            }
            case "APPROVED":
            {
                st_ = Status.APPROVED;
                break;
            }
            case "ORDERED_AND_WAITING_FOR_ARRIVAL":
            {
                st_ = Status.ORDERED_AND_WAITING_FOR_ARRIVAL;
                break;
            }
            case "CLOSE":
            {
                st_ = Status.CLOSE;
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    @Override
    public Boolean upload()
    {
        Boolean result;
        if(super.hasNewData())
        {
            result = super.uploadToDb(ChineseSt(st_));
            super.clearUpdated();
        }
        else
        {
            result = Boolean.TRUE;
        }
        return result;
    }
}
