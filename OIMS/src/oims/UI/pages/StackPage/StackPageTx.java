/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.StackPage;

import oims.support.util.SqlDataTable;
import oims.support.util.UnitQuantity;

/**
 *
 * @author ezouyyi
 */
public interface StackPageTx {
    public Integer createCiTicket(String rawMaterialId, UnitQuantity quantity, String wareHouseId, 
            String employeeIdSubmitor, String unitPrice, String totalPrice);
    public Integer createCoTicket(String rawMaterialId, UnitQuantity quantity, String wareHouseId,
            String employeeIdSubmitor);
    public Boolean errReportInventoryCheck(Boolean isInventoryLack, String rawMaterialId, 
            UnitQuantity quantity, String employeeIdSubmitor);
    public Boolean rawMaterialDiscard(String rawMaterialId, UnitQuantity quantity, String employeeIdSubmitor);
    public SqlDataTable queryCiTicket();
    public SqlDataTable queryCoTicket();
    public SqlDataTable queryWarehousStack();
}
