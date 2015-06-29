/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.warehouseManagerment;

import oims.support.util.SqlDataTable;

/**
 *
 * @author ezouyyi
 */
public interface WarehousePageTx {
    public String newWareHouse(String wareHouseName, Integer keeperId, String addr, String contact);
    public Boolean updateWareHouse(Integer wareHouseId, String wareHouseName, Integer keeperId,
            String addr);
    public Boolean deleteWareHouse(Integer wareHouseId);
    public void    setRx(WarehousePageRx rx);
    public SqlDataTable queryAllWarehouseInfo();
}
