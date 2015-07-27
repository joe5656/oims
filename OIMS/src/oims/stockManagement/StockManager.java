/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.stockManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public SqlResultInfo checkIn(Integer whId, String whName, Integer rawMaterialId, 
            String rawMaterialName, UnitQuantity uq)
    {
        return itsStockTable_.rawMaterialCheckIn(whName, whId, rawMaterialId, rawMaterialName, uq, Boolean.TRUE);
    }
 
    public SqlResultInfo checkOut(Integer whid, Integer rawMaterialId, UnitQuantity uq)
    {
        return itsStockTable_.rawMaterialCheckOut(whid, rawMaterialId, uq);
    }
    
    public String[] queryMaterialStockUnit(String whId, String rmName)
    {
        return this.itsStockTable_.queryMaterialStockUnit(whId, rmName);
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
    
    public SqlDataTable queryStock(String whId, String rmName, String unit)
    {
        SqlResultInfo result = this.itsStockTable_.query(whId, rmName,unit);
        SqlDataTable returnValue = new SqlDataTable(result.getResultSet(), this.itsStockTable_.getName());
        this.itsStockTable_.translateColumnName(returnValue.getColumnNames());
        return returnValue;
    }
    
    // call modifyStock if you really want to MODIFY a exiting stock not
    // create a new one when the record is not exits
    public SqlResultInfo modifyStock(Integer whId, String warehouseName, String rawMaterialName,
            Integer rawMaterialId, String modifyNumber, Boolean add, String unit)
    {
        if(add)
        {
            return this.itsStockTable_.rawMaterialCheckIn(warehouseName, whId, rawMaterialId, 
                    rawMaterialName, new UnitQuantity(unit, Double.parseDouble(modifyNumber)), false);
        }
        else
        {
            return this.itsStockTable_.rawMaterialCheckOut(whId, rawMaterialId, 
                    new UnitQuantity(unit, Double.parseDouble(modifyNumber)));
        }
    }
}
