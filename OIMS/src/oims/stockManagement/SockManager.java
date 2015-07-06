/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.stockManagement;

import oims.dataBase.tables.StockTable;
import oims.rawMaterialManagement.RawMaterial;
import oims.support.util.UnitQuantity;

/**
 *
 * @author ezouyyi
 */
public class SockManager {
    StockTable     itsStackTable_;
    
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
}
