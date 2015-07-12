/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.warehouseManagemnet;
import oims.dataBase.tables.WareHouseTable;
import com.google.common.collect.Maps;
import java.util.Map;
import oims.UI.UiManager;
import oims.UI.pages.warehouseManagerment.WarehousePageRx;
import oims.UI.pages.warehouseManagerment.WarehousePageTx;
import oims.UI.pages.warehouseManagerment.WarehousePickerTx;
import oims.dataBase.DataBaseManager;
import oims.systemManagement.SystemManager;
import oims.stockManagement.StockManager;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
/**
 *
 * @author ezouyyi
 */
public class WareHouseManager implements oims.systemManagement.Client, WarehousePageTx{
    private Map<Integer, String> wareHouses_;
    private WareHouseTable  itsWareHouseTable_;
    private SystemManager    itsSysManager_;
    private StockManager    itsStackManager_;
    private WarehousePageRx itsWarehousePageRx_;
    public WareHouseManager(DataBaseManager dbm, StockManager stackM)
    {
        wareHouses_ = Maps.newHashMap();
        itsWareHouseTable_ = new WareHouseTable(dbm);
        itsStackManager_ = stackM;
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "warehouse");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "warehouse");
                break;
            }
            case SYS_REGISTER:
            {
                itsWareHouseTable_.registerToDbm();
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "warehouse");
                break;
            }
        }
        return Boolean.TRUE;
    }
    
    @Override
    public void setSystemManager(SystemManager sysManager)
    {
        itsSysManager_ = sysManager;
    }
    
    /*
    public Boolean registerNewRawMaterial(String name, double normalPrice, CommonUnit priceUnit)
    {
        return Objects.equals(this.itsRawMaterialTable_.newEntry(normalPrice, priceUnit, name), Boolean.TRUE);
    }
    
    public Boolean rawMaterialCheckIn(WareHouse wh, RawMaterial rawMaterial, 
                        CommonUnit unit, Double quantity)
    {
        //syncd means connect to DB and download data sucessfully
        if(rawMaterial.isSyncd())
        {
            UnitQuantity uq = new UnitQuantity(unit, quantity);
            return wh.checkIn(rawMaterial, uq);
        }
        return Boolean.FALSE;
    }
    
    public Boolean rawMaterialCheckOut(WareHouse wh, RawMaterial material, 
                        CommonUnit unit, Double quantity)
    {return false;}*/
    @Override
    public SqlResultInfo deleteWareHouse(Integer warehouseId)
    {
        SqlResultInfo returnValue = new SqlResultInfo(Boolean.FALSE);
        if(itsStackManager_.isStackEmptryForWarehouse(warehouseId))
        {
            returnValue = itsWareHouseTable_.removeEntry(warehouseId);
        }
        else
        {
            returnValue.setErrInfo("仓库库存不为空");
        }
        return returnValue;
    }

    @Override
    public SqlResultInfo newWareHouse(String wareHouseName, Integer keeperId, String addr, String contact) {
        return itsWareHouseTable_.NewEntry(wareHouseName, keeperId.toString(), addr, contact); 
    }

    @Override
    public Boolean updateWareHouse(Integer wareHouseId, String wareHouseName, Integer keeperId, String addr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRx(WarehousePageRx rx) {itsWarehousePageRx_=rx;}

    @Override
    public SqlDataTable queryAllWarehouseInfo() {
        SqlResultInfo rs = this.itsWareHouseTable_.query(null, null, null, null, null);
        SqlDataTable  dTable = new SqlDataTable(rs.getResultSet(),this.itsWareHouseTable_.getName());
        this.itsWareHouseTable_.translateColumnName(dTable.getColumnNames());
        return dTable;
    }
    
    public void needWarehousePicker(WarehousePickerTx tx)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        tempUiM.showWarehousePicker(this.queryAllWarehouseInfo(), tx);
    }
    
    public Integer getKeeperId(Integer whId)
    {
        return 3;
    }
    
    static public Integer getDumpingWarehouseId(){return 999999;}
}
