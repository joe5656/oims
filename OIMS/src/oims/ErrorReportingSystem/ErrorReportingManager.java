/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.ErrorReportingSystem;

import oims.ErrorReportingSystem.errHandlers.ErrHandler;
import oims.ErrorReportingSystem.errHandlers.SalingFaultErrHandler;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.ErrorReportingTable;
import oims.employeeManager.Employee;
import oims.stockManagement.StockManager;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class ErrorReportingManager implements oims.systemManagement.Client{
    private DataBaseManager  itsdbm_;
    private SystemManager    itsSysManager_;
    private ErrorReportingTable itsErrReportingTable_;
    private StockManager     itsStockManager_;
    public enum ERR_TYPE
    {
        ERR_TYPE_NOT_ENOUGH_PRODUCT,
        ERR_TYPE_NOT_ENOUGH_CK_DELIVERY,
        ERR_TYPE_SALING_FAULT
    }
    
    private ERR_TYPE getErrType(String type)
    {
        ERR_TYPE result = null;
        switch(type)
        {
            case "ERR_TYPE_SALING_FAULT":
            {
                result = ERR_TYPE.ERR_TYPE_SALING_FAULT;
                break;
            }
        }
        return result;
    }
    
    public SqlResultInfo reportSalingErr(String errOccuDate/*yyyy-mm-dd*/, 
            Boolean isMoreThanRecord, String pName, String numberOfErr,
            String responserName,String commnet, String reponserId)
    {
        Employee submitor = this.itsSysManager_.getCurEmployee();
        String spec = this.serializeSaleErrReport(isMoreThanRecord, pName, numberOfErr);
        SqlResultInfo result = this.itsErrReportingTable_.NewEntry(spec, ERR_TYPE.ERR_TYPE_SALING_FAULT.toString(),
                submitor.getId().toString(), submitor.getName(), commnet, errOccuDate, responserName, reponserId);
        
        if(result.isSucceed())
        {
            // report to StoreStock manager to update StoreStock
        }
        return result;
    }
    
    private String serializeSaleErrReport(Boolean isMoreThanRecord, String pName, String numberOfErr)
    {
        return "isMoreThanRecord="+(isMoreThanRecord?"1":"0")+"|productName="+pName+"|numberOfErro="+numberOfErr;
    }
    
    private void unserializeSaleErrReport(String inputString, Boolean isMoreThanRecord, 
            String pName, String numberOfErr)
    {
        if(inputString != null)
        {
            String[] output = inputString.split("|");
            for(String entry:output)
            {
                String[] tmp = entry.split(":");
                switch(tmp[0])
                {
                    case "isMoreThanRecord":
                    {
                        isMoreThanRecord = tmp[1]=="1"?true:false;
                        break;
                    }
                    case "productName":
                    {
                        pName = tmp[1];
                        break;
                    }
                    case "numberOfErro":
                    {
                        numberOfErr = tmp[1];
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status) 
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
            case SYS_REGISTER:
            {
                this.itsdbm_.registerTable(itsErrReportingTable_);
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "sm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager){itsSysManager_ = sysManager;}
    
}
