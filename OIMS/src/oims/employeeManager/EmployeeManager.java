/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.employeeManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.UI.UiManager;
import oims.UI.pages.employeePage.EmployeePageRx;
import oims.UI.pages.employeePage.EmployeePageTx;
import oims.UI.pages.employeePage.EmployeePickerTx;
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
    public SqlDataTable queryEmployeeInfo(String employeeId) 
    {
        SqlResultInfo rs = this.itsEmployeeTable_.queryEmployeeGeneralInfo(employeeId,null,null);
        SqlDataTable dTable = null;
        if(rs.isSucceed())
        {
            dTable = new SqlDataTable(rs.getResultSet(),EmployeeTable.getDerivedTableName());
        }
        return dTable;
    }

    @Override
    public Vector requestColumns() {
        Vector returnVector = this.itsEmployeeTable_.getColumns();
        this.itsEmployeeTable_.translateColumnName(returnVector);
        return returnVector;
    }

    @Override
    public SqlDataTable queryGenerallEmployeeInfo(Boolean isActive) {
        SqlResultInfo rs = this.itsEmployeeTable_.queryEmployeeGeneralInfo(null,null,isActive);
        SqlDataTable dTable = null;
        if(rs.isSucceed())
        {
            dTable = new SqlDataTable(rs.getResultSet(),EmployeeTable.getDerivedTableName());
        }
        return dTable;
    }
    
    public void needEmployeePicker(EmployeePickerTx tx, Integer id, Boolean isActive)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        tempUiM.showEmployeePicker(this.queryGenerallEmployeeInfo(isActive), tx, id);
    }
    
    public String getEmployeeName(Integer id)
    {
        String name = "err";
        SqlResultInfo result = this.itsEmployeeTable_.queryEmployeeGeneralInfo(null, id.toString(), Boolean.TRUE);
        if(result.isSucceed())
        {
            ResultSet rs = result.getResultSet();
            try {
                if(rs.first())
                {
                    name = rs.getString(EmployeeTable.getEmployeeNameColNameInEng());
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return name;
    }
    
    static public Integer getInvalidEmployeeId(){return 99999999;}
    static public Integer getRobetId(){return 88888888;}
    static public String  getRobetName(){return "systemAuto";}

    @Override
    public Boolean checkPassword(String id, String pw) {
       return this.itsEmployeeTable_.checkPassword(id, pw);
    }

    @Override
    public SqlResultInfo updatePassword(String id, String pw) {
        return this.itsEmployeeTable_.update(id, null, null, null, null, null, null, null, pw);
    }

    @Override
    public Boolean toggleEmployee(String id, Boolean active) {
        SqlResultInfo result = this.itsEmployeeTable_.update(id, null, null, null, null, null, null, active, null);
        return result.isSucceed();
    }
    
    public Boolean authEmployee(String id, String pw)
    {
        return this.itsEmployeeTable_.checkPassword(id, pw);
    }
    
    public Employee getEmployee(String id)
    {
        Employee e = new Employee();
        this.itsEmployeeTable_.serializeEmployee(e, id);
        return e;
    }
}
