/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.employeeManagerment;

import java.util.List;
import java.util.Vector;
import oims.employeeManager.Employee;

/**
 *
 * @author ezouyyi
 */
public interface EmployeePageTx {
    public void         setRx(EmployeePageRx rx);
    public Boolean      createNewEmploye(Employee employee);
    public Boolean      changeEmployeeInformation(Employee employee);
    public Employee     queryEmployeeInfo(String employeeId);
    public Vector requestColumns();
}
