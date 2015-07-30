/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.SellingSystem;

import com.google.common.collect.Maps;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.SellTicketDetailTable;
import oims.dataBase.tables.SellTicketTable;
import oims.productManagement.Product;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class SellingManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private DataBaseManager  itsdbm_;
    private SellTicketDetailTable itsDetailTable_;
    private SellTicketTable  itsTicketTable_;
    
    public SellingManager(DataBaseManager dbm)
    {
        itsDetailTable_ = new SellTicketDetailTable(dbm);
        itsTicketTable_ = new SellTicketTable(dbm);
        itsdbm_  = dbm;
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
                this.itsdbm_.registerTable(itsDetailTable_);
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
        return Boolean.TRUE;}

    @Override
    public void setSystemManager(SystemManager sysManager){itsSysManager_ = sysManager;}
    
    public Map<String, Product> buildProductMap()
    {
        Map<String, Product> result = Maps.newHashMap();
        return result;
    }
            
    public Boolean placeOrder()
    {
        return false;
    }
    
    
}
