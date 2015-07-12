/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.employeePage;

import java.util.List;
import java.util.Vector;
import oims.employeeManager.Employee;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author ezouyyi
 */
public interface EmployeePageTx {
    public void           setRx(EmployeePageRx rx);
    public SqlResultInfo  createNewEmploye(Employee employee);
    public SqlResultInfo  changeEmployeeInformation(Employee employee);
    public SqlDataTable   queryEmployeeInfo(String employeeId);
    public Vector         requestColumns();
    public SqlDataTable   queryGenerallEmployeeInfo();
    public Boolean        checkPassword(String id, String pw);
    public SqlResultInfo  updatePassword(String id, String pw);
    public Boolean        toggleEmployee(String id, Boolean active);
}
