/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI;

import oims.UI.pages.Page;
import oims.UI.pages.ProductPage.ProductPickerTx;
import oims.UI.pages.employeePage.EmployeePickerTx;
import oims.UI.pages.loginPage.loginPageTx;
import oims.UI.pages.rawMaterialPage.RawMaterialPickerTx;
import oims.UI.pages.reciptPage.DetailReciptPickerTx;
import oims.UI.pages.reciptPage.ProductReciptPickerTx;
import oims.UI.pages.warehouseManagerment.WarehousePickerTx;
import oims.employeeManager.EmployeeManager;
import oims.support.util.SqlDataTable;

/**
 *
 * @author ezouyyi
 */
public interface UiManagerRx {
    public enum PageType
    {
        INIT_PAGE,
        MAIN_PAGE,
        WAREHOUSE_PAGE,
        EMPLOYEE_PAGE,
        STACK_PAGE,
        RAWMATERIAL_PAGE,
        STORE_PAGE,
        PRODUCT_PAGE,
        RECIPT_PAGE
    };
    public Page getPage(PageType pt);
    public void showPage(Integer pageId);
    public void showEmployeePicker(SqlDataTable table, EmployeePickerTx tx, Integer identify);
    public void showProductPicker(SqlDataTable table, ProductPickerTx tx, Integer identify);
    public void showDetailReciptPicker(SqlDataTable table, DetailReciptPickerTx tx, Integer identify);
    public void showrProductReciptPicker(SqlDataTable table, ProductReciptPickerTx tx, Integer identify);
    public void showWarehousePicker(SqlDataTable table, WarehousePickerTx tx, Integer identify);
    public void showRawMaterialPicker(SqlDataTable table, RawMaterialPickerTx tx, Integer identify);
    public void showLoginPage(loginPageTx tx);
}
