/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.UI.pages.loginPage;

import oims.employeeManager.Employee;

/**
 *
 * @author ezouyyi
 */
public interface loginPageTx {
    public void userUnauth();
    public void userAuth(Employee authedEmployee) ;
}
