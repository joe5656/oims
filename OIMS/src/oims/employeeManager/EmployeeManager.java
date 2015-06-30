/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.employeeManager;

import java.util.Vector;
import oims.UI.pages.employeeManagerment.EmployeePageRx;
import oims.UI.pages.employeeManagerment.EmployeePageTx;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.EmployeeTable;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class EmployeeManager implements oims.systemManagement.Client,EmployeePageTx{
    private final EmployeeTable itsEmployeeTable_;
    private SystemManager itsSysManager_;
    private EmployeePageRx itsPageRx_;
    public EmployeeManager(DataBaseManager dbm)
    {
        itsEmployeeTable_ = new EmployeeTable(dbm);
    }
    
    public SqlResultInfo createNewEmployee(Employee employee)
    {
        return this.itsEmployeeTable_.newEntry(employee);
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
            case SYS_REGISTER:
            {
                itsEmployeeTable_.registerToDbm();
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager) {
        itsSysManager_ = sysManager;}

    @Override
    public void setRx(EmployeePageRx rx) {itsPageRx_ = rx;}

    @Override
    public SqlResultInfo createNewEmploye(Employee employee) {return this.itsEmployeeTable_.newEntry(employee);}

    @Override
    public SqlResultInfo changeEmployeeInformation(Employee employee) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee queryEmployeeInfo(String employeeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector requestColumns() {
        Vector returnVector = this.itsEmployeeTable_.getColumns();
        this.itsEmployeeTable_.translateColumnName(returnVector);
        return returnVector;
    }

    @Override
    public SqlDataTable queryGenerallEmployeeInfo() {
        SqlResultInfo rs = this.itsEmployeeTable_.queryAllEmployeeGeneralInfo();
        SqlDataTable dTable = null;
        if(rs.isSucceed())
        {
            dTable = new SqlDataTable(rs.getResultSet(),EmployeeTable.getDerivedTableName());
        }
        return dTable;
    }
}
