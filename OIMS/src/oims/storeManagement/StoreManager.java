/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.storeManagement;

import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.RawMaterialTable;
import oims.dataBase.tables.StoreTable;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class StoreManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private final StoreTable itsStoreTable_;
    private DataBaseManager  itsdbm_;
    
    public StoreManager(DataBaseManager dbm)
    {
        itsStoreTable_ = new StoreTable(dbm);
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
                this.itsdbm_.registerTable(itsStoreTable_);
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
    
    public SqlResultInfo createStore(String storeName, String StoreManagerName, Integer StoreManagerId,
            String addr, String contact, String province, String city)
    {
        return this.itsStoreTable_.NewEntry(storeName, StoreManagerName, StoreManagerId, addr, contact, province, city);
    }
    
    public SqlDataTable queryStores(String storeName, String StoreManagerName, Integer StoreManagerId, String city, String province)
    {
        SqlDataTable table = new SqlDataTable(this.itsStoreTable_.query(storeName, StoreManagerName, StoreManagerId, province, city).getResultSet(),this.itsStoreTable_.getName());
        this.itsStoreTable_.translateColumnName(table.getColumnNames());
        return table;
    }
    
    public SqlResultInfo updateStore(String storeId, String storeName, 
            String StoreManagerName, Integer StoreManagerId,String contact)
    {
        return this.itsStoreTable_.update(storeId, storeName, StoreManagerName, StoreManagerId, contact);
    }
}
