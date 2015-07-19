/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.stockManagement;

import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.StockTable;
import oims.rawMaterialManagement.RawMaterial;
import oims.support.util.UnitQuantity;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class StockManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private StockTable       itsStackTable_;
    private DataBaseManager  itsdbm_;
    
    public StockManager(DataBaseManager dbm)
    {
        itsStackTable_ = new StockTable(dbm);
        itsdbm_  = dbm;
    }    
    
    public Boolean isStackEmptryForWarehouse(Integer warehouseId){return Boolean.TRUE;}
    public Boolean checkIn(Integer whId, String whName, Integer rawMaterialId, 
            String rawMaterialName, UnitQuantity uq)
    {
        return itsStackTable_.rawMaterialCheckIn(whId, whName, rawMaterialId, rawMaterialName, uq);
    }
 
    public Boolean checkOut(Integer whid, RawMaterial rawMaterial, UnitQuantity uq)
    {
        return itsStackTable_.rawMaterialCheckOut(whid, rawMaterial, uq);
    }

    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status) {
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
                this.itsdbm_.registerTable(itsStackTable_);
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
}
