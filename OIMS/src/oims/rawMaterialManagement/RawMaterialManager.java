/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.rawMaterialManagement;

import oims.UI.UiManager;
import oims.UI.pages.rawMaterialPage.RawMaterialPickerTx;
import oims.UI.pages.warehouseManagerment.WarehousePickerTx;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.RawMaterialTable;
import oims.support.util.CommonUnit;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class RawMaterialManager implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private final RawMaterialTable itsRawMaterialTable_;
    private DataBaseManager  itsdbm_;
    
    public RawMaterialManager(DataBaseManager dbm)
    {
        itsRawMaterialTable_ = new RawMaterialTable(dbm);
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
                this.itsdbm_.registerTable(itsRawMaterialTable_);
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
    public void setSystemManager(SystemManager sysManager) {itsSysManager_ = sysManager;}
    
    public SqlResultInfo newRawMaterial(String name, CommonUnit unit, Double price) {
        return this.itsRawMaterialTable_.newEntry(price, unit, name);
    }
    
    public SqlResultInfo udpate(Integer id, CommonUnit unit, String name, Double price, Boolean valid) {
        return this.itsRawMaterialTable_.updateEntry(id, name, unit, valid, price);
    }
    
    public SqlDataTable query(Integer id, Boolean valid) 
    {
        SqlResultInfo rs = this.itsRawMaterialTable_.query(id, valid);
        SqlDataTable  dTable = new SqlDataTable(rs.getResultSet(),this.itsRawMaterialTable_.getName());
        this.itsRawMaterialTable_.translateColumnName(dTable.getColumnNames());
        return dTable;
    }
    
    public void needRawMaterialPickerAll(RawMaterialPickerTx tx)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        tempUiM.showRawMaterialPicker(this.query(null, null), tx);
    }
    
    public void needRawMaterialPickerValidAll(RawMaterialPickerTx tx)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        tempUiM.showRawMaterialPicker(this.query(null, Boolean.TRUE), tx);
    }
}
