/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.reciptManagement;

import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.DetailReciptTable;
import oims.dataBase.tables.ProductReciptTable;
import oims.dataBase.tables.RawMaterialTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class ReciptManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private final ProductReciptTable itsProductReciptTable_;
    private final DetailReciptTable  itsDetailReciptTable_;
    private DataBaseManager  itsdbm_;
    
    public ReciptManager(DataBaseManager dbm)
    {
        itsProductReciptTable_ = new ProductReciptTable(dbm);
        itsDetailReciptTable_  = new DetailReciptTable(dbm);
        itsdbm_ = dbm;
    }
        
    public SqlResultInfo newProductRecipt(Integer pid, String pName, Integer number, String mainName,
            String topName, String fillingName, Integer workinghours)
    {
        return this.itsProductReciptTable_.newEntry(pid, pName, number, mainName, topName, fillingName, workinghours);
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
            case SYS_REGISTER:
            {
                this.itsdbm_.registerTable(itsProductReciptTable_);
                this.itsdbm_.registerTable(itsDetailReciptTable_);
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager){itsSysManager_= sysManager;}
    
}
