/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.storeStockManagement;

import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.StoreStackTable;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;


/**
 *
 * @author ezouyyi
 */
public class StoreStackManager implements oims.systemManagement.Client{
    private final StoreStackTable itsStoreStackTable_;
    private SystemManager    itsSysManager_;
    private final DataBaseManager  itsdbm_;
    
    public StoreStackManager(DataBaseManager dbm)
    {
        itsStoreStackTable_ = new StoreStackTable(dbm);
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
                this.itsdbm_.registerTable(itsStoreStackTable_);
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
    public void setSystemManager(SystemManager sysManager){itsSysManager_ = sysManager;}
    
    public SqlDataTable getStoreStock(String storeName, String productName)
    {
        SqlResultInfo result = this.itsStoreStackTable_.query(storeName, productName);
        if(result.isSucceed())
        {
            return new SqlDataTable(result.getResultSet(),this.itsStoreStackTable_.getName());
        }
        else
        {
            return new SqlDataTable();
        }
    }
    
    public SqlResultInfo updateStoreStock(String storeName, String productName, Double updateQuantity)
    {
        return this.itsStoreStackTable_.updateStoreStock(storeName, productName, updateQuantity);
    }
}
