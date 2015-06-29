/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.warehouseManagerment;

/**
 *
 * @author ezouyyi
 */
public interface WarehousePageTx {
    public String newWareHouse(String wareHouseName, Integer keeperId, String addr, String contact);
    public Boolean updateWareHouse(Integer wareHouseId, String wareHouseName, Integer keeperId,
            String addr);
    public Boolean deleteWareHouse(Integer wareHouseId);
}
