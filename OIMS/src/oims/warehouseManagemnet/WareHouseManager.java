/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.warehouseManagemnet;
import oims.dataBase.tables.WareHouseTable;
import oims.dataBase.tables.RawMaterialTable;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.StackTable;
import oims.support.util.CommonUnit;
import oims.support.util.UnitQuantity;
import oims.systemManagement.SystemManager;
/**
 *
 * @author ezouyyi
 */
public class WareHouseManager implements oims.systemManagement.Client{
    Map<Integer, String> wareHouses_;
    WareHouseTable  itsWareHouseTable_;
    RawMaterialTable itsRawMaterialTable_;
    StackTable       itsStackTable_;
    SystemManager    itsSysManager_;
   
    public WareHouseManager(SystemManager sm, DataBaseManager dbm)
    {
        wareHouses_ = Maps.newHashMap();
        itsSysManager_ = sm;
        itsStackTable_ = new StackTable(dbm);
        itsWareHouseTable_ = new WareHouseTable(dbm);
        itsRawMaterialTable_ = new RawMaterialTable(dbm);
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {return Boolean.FALSE;}
    
    @Override
    public void setSystemManager(SystemManager sysManager)
    {
        itsSysManager_ = sysManager;
    }
    
    public Boolean registerNewWareHouse(String wareHouseName, String keeper,
            String addr, String contact)
    {
        return Objects.equals(itsWareHouseTable_.NewEntry(wareHouseName, keeper, addr, contact), Boolean.TRUE); 
    }
    
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
    {return false;}
    
    public Boolean deleteWareHouse(Integer warehouseId)
    {
        Boolean returnValue = Boolean.FALSE;
        if(itsStackTable_.isStackEmptryForWarehouse(warehouseId))
        {
            returnValue = itsWareHouseTable_.removeEntry(warehouseId);
            //itsStackTable_.removeStackRecordFromWarehouse(warehouseId);
        }
        return returnValue;
    }
}
