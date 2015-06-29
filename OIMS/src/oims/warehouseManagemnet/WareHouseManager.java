/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.warehouseManagemnet;
import oims.dataBase.tables.WareHouseTable;
import com.google.common.collect.Maps;
import java.util.Map;
import oims.UI.pages.warehouseManagerment.WarehousePageTx;
import oims.dataBase.DataBaseManager;
import oims.systemManagement.SystemManager;
import oims.stackManagement.StackManager;
/**
 *
 * @author ezouyyi
 */
public class WareHouseManager implements oims.systemManagement.Client, WarehousePageTx{
    Map<Integer, String> wareHouses_;
    WareHouseTable  itsWareHouseTable_;
    SystemManager    itsSysManager_;
    StackManager    itsStackManager_;
    public WareHouseManager(SystemManager sm, DataBaseManager dbm)
    {
        wareHouses_ = Maps.newHashMap();
        itsSysManager_ = sm;
        itsWareHouseTable_ = new WareHouseTable(dbm);
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {return Boolean.FALSE;}
    
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
    public Boolean deleteWareHouse(Integer warehouseId)
    {
        Boolean returnValue = Boolean.FALSE;
        if(itsStackManager_.isStackEmptryForWarehouse(warehouseId))
        {
            returnValue = itsWareHouseTable_.removeEntry(warehouseId);
            //itsStackTable_.removeStackRecordFromWarehouse(warehouseId);
        }
        return returnValue;
    }

    @Override
    public String newWareHouse(String wareHouseName, Integer keeperId, String addr, String contact) {
        itsWareHouseTable_.NewEntry(wareHouseName, keeperId.toString(), addr, contact); 
        return "warehouseid";
    }

    @Override
    public Boolean updateWareHouse(Integer wareHouseId, String wareHouseName, Integer keeperId, String addr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
