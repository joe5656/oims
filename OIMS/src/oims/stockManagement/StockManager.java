/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.stockManagement;

import java.util.Vector;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.RawMaterialTable;
import oims.dataBase.tables.StockTable;
import oims.rawMaterialManagement.RawMaterial;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.support.util.UnitQuantity;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class StockManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private StockTable       itsStockTable_;
    private DataBaseManager  itsdbm_;
    
    public StockManager(DataBaseManager dbm)
    {
        itsStockTable_ = new StockTable(dbm);
        itsdbm_  = dbm;
    }    
    
    public Boolean isStackEmptryForWarehouse(Integer warehouseId){return Boolean.TRUE;}
    public Boolean checkIn(Integer whId, String whName, Integer rawMaterialId, 
            String rawMaterialName, UnitQuantity uq)
    {
        return itsStockTable_.rawMaterialCheckIn(whId, whName, rawMaterialId, rawMaterialName, uq);
    }
 
    public Boolean checkOut(Integer whid, RawMaterial rawMaterial, UnitQuantity uq)
    {
        return itsStockTable_.rawMaterialCheckOut(whid, rawMaterial, uq);
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
                this.itsdbm_.registerTable(itsStockTable_);
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
    
    public SqlDataTable queryStock(String whId, String rmName)
    {
        SqlResultInfo result = this.itsStockTable_.query(whId, rmName);
        SqlDataTable returnValue = new SqlDataTable(result.getResultSet(), this.itsStockTable_.getName());
        this.itsStockTable_.translateColumnName(returnValue.getColumnNames());
        return returnValue;
    }
    
    public String queryMaterialStockUnit(String whId, String rmName)
    {
        SqlDataTable result = this.queryStock(whId, rmName);
        String returnValue = null;
        if(result.getColumnNames().size() > 0)
        {
            Vector head = result.getColumnNames();
            Integer unitIndex = head.indexOf(RawMaterialTable.getUnitNameColNameInCh());
            Vector data = (Vector)result.getSelectedRows().get(0);
            returnValue = (String)data.get(unitIndex);
        }
        return returnValue;
    }
    
    public SqlResultInfo modifyStock(String warehouseName, String rawMaterialName,
            String modifyNumber, String unit)
    {
        
    }
}
