/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ticketSystem.Tickets;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import oims.dataBase.tables.TicketTable;
import oims.employeeManager.Employee;
import oims.support.util.UnitQuantity;
import oims.ticketSystem.Ticket;

/**
 *
 * @author ezouyyi
 */
public class WareHouseCoTicket  extends Ticket {
    Map<Integer, groupedRMInfo> rawMaterialList_;
    Status      st_;

    public WareHouseCoTicket(TicketTable table) {
        super(table, TicketType.WAREHOUSETICKET_CO);
        rawMaterialList_ = Maps.newHashMap();
    }
    
    private class groupedRMInfo
    {
        String rawMaterialName_;
        UnitQuantity quantity_;
        
        public groupedRMInfo(String name, UnitQuantity uq)
        {
            rawMaterialName_ = name;
            quantity_ = uq;
        }
        
        UnitQuantity getQuantity(){return quantity_;}
        String getName(){return rawMaterialName_;}
    }
    
    enum Status
    {
        SUBMITTED,
        APPROVED,
        DISTRUBUTED, /*checked out from wh, waiting for recevier to close this ticket*/
        CLOSE
    }
    
    /**
     * create formated string for specified area
     * format: rawMaterialId(1):unit:quantity:rawMaterialName(1)
     *         |....
     *         |rawMaterialId(N):unit:rawMaterialName(N)
     * @return 
     */
    private String subSerilze()
    {
        Set<Map.Entry<Integer, groupedRMInfo>> entrySet = rawMaterialList_.entrySet();
        Iterator<Map.Entry<Integer, groupedRMInfo>> itr = entrySet.iterator();
        String returnString = "";
        while(itr.hasNext())
        {
            Map.Entry<Integer, groupedRMInfo> tmpEntry = itr.next();
            if(!"".equals(returnString))
            {
                returnString += "|";
            }
            returnString += tmpEntry.getValue().toString() + ":" 
                    + tmpEntry.getValue().getQuantity().getUnit().getUnitName() + ":"
                    + tmpEntry.getValue().getQuantity().getQuantity().toString() + ":"
                    + tmpEntry.getValue().getName();
            
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
                        new groupedRMInfo(details[4], new UnitQuantity(details[1], Double.parseDouble(details[2]))));
            }
            returnValue = Boolean.TRUE;
        }
        return returnValue;
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
                result = "审核通过，待仓库配送";
                break;
            }
            case DISTRUBUTED:
            {
                result = "已配送";
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
                st_ = Status.DISTRUBUTED;
                super.setOwnerId(nextOwner.getId());
                super.setOwnerName(nextOwner.getName()); 
                setUpdated();
                break;
            }
            case DISTRUBUTED:
            {
                canMove = Boolean.TRUE;
                st_ = Status.CLOSE;
                super.setOwnerId(nextOwner.getId());
                super.setOwnerName(nextOwner.getName()); 
                setUpdated();
                break;
            }
        }
        super.addHistory("单据状态改变到： "+ChineseSt(st_));
        return canMove;
    };
    
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
            case "DISTRUBUTED":
            {
                st_ = Status.DISTRUBUTED;
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
