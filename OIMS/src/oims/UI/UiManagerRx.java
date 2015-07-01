/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI;

import oims.UI.pages.Page;
import oims.UI.pages.employeeManagerment.EmployeePickerTx;
import oims.UI.pages.warehouseManagerment.WarehousePickerTx;
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
        STACK_PAGE
    };
    public Page getPage(PageType pt);
    public void showPage(Integer pageId);
    public void showEmployeePicker(SqlDataTable table, EmployeePickerTx tx);
    public void showWarehousePicer(SqlDataTable table, WarehousePickerTx tx);
}
