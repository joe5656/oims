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
import oims.storeStockManagement.StoreStackManager;
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
    private StoreStackManager itsStoreStackManager_;
    
    public ErrorReportingManager(DataBaseManager dbm)
    {
        itsErrReportingTable_ = new ErrorReportingTable(dbm);
        itsdbm_  = dbm;
    }  
    public enum ERR_TYPE
    {
        ERR_TYPE_NOT_ENOUGH_PRODUCT,
        ERR_TYPE_CK_DELIVERY_FAULT,
        ERR_TYPE_SALING_FAULT,
        ERR_TYPE_PRODUCNIG_FAULT,
        ERR_TYPE_WAREHOUSE_CHECK_FAILURE,
        ERR_TPE_RAWMATERIAL_SCRAP
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
    
    public SqlResultInfo reportWareHouseCheckErr(String errOccuDate/*yyyy-mm-dd*/, 
            Boolean isMoreThanRecorded, String rawMaterialname, String ErrNumber, String unit,
            String responserName,String commnet, String reponserId, String warehouseName)
    {
        Employee submitor = this.itsSysManager_.getCurEmployee();
        String spec = this.serializeWarehouseCheckErrReport(isMoreThanRecorded, rawMaterialname, ErrNumber, warehouseName, unit);
        SqlResultInfo result = this.itsErrReportingTable_.NewEntry(spec, ERR_TYPE.ERR_TYPE_WAREHOUSE_CHECK_FAILURE.toString(),
                submitor.getId().toString(), submitor.getName(), commnet, errOccuDate, responserName, reponserId);
        if(result.isSucceed())
        {
            // report to StoreStock manager to update StoreStock
            this.itsStockManager_.modifyStock(null, warehouseName, rawMaterialname, null, ErrNumber, isMoreThanRecorded, unit);
            
        }
        return result;        
    }
    
    private String serializeWarehouseCheckErrReport(Boolean isMoreThanRecorded, 
            String rawMaterialname, String ErrNumber, String warehouseName, String unit)
    {
        return "isMoreThanRecorded="+(isMoreThanRecorded?"1":"0")+"|rawMaterialname="
                +rawMaterialname+"|ErrNumber="+ErrNumber+"|unit="+unit+"|warehouseName="+warehouseName;
    }    
    
    public SqlResultInfo reportCkDeliveryErr(String errOccuDate/*yyyy-mm-dd*/, 
            Boolean isMoreThanPlaned, String reciptName, String ErrNumber,
            String responserName,String commnet, String reponserId, String storeName)
    {
        Employee submitor = this.itsSysManager_.getCurEmployee();
        String spec = this.serializeCkDeliveryErrReport(isMoreThanPlaned, reciptName, ErrNumber, storeName);
        SqlResultInfo result = this.itsErrReportingTable_.NewEntry(spec, ERR_TYPE.ERR_TYPE_CK_DELIVERY_FAULT.toString(),
                submitor.getId().toString(), submitor.getName(), commnet, errOccuDate, responserName, reponserId);
        return result;
    }
    
    private String serializeCkDeliveryErrReport(Boolean isMoreThanPlaned, String reciptName
                ,String numberOfErr, String storeName)
    {
        return "isMoreThanPlaned="+(isMoreThanPlaned?"1":"0")+"|reciptName="
                +reciptName+"|numberOfErro="+numberOfErr+"|storeName="+storeName;
    }
    
    private void unserializeCkDeliveryErrReport(String inputString, Boolean isMoreThanPlaned, 
            String reciptName, String numberOfErr, String storeName)
    {
        if(inputString != null)
        {
            String[] output = inputString.split("|");
            for(String entry:output)
            {
                String[] tmp = entry.split(":");
                switch(tmp[0])
                {
                    case "isMoreThanPlaned":
                    {
                        isMoreThanPlaned = "1".equals(tmp[1]);
                        break;
                    }
                    case "reciptName":
                    {
                        reciptName = tmp[1];
                        break;
                    }
                    case "numberOfErro":
                    {
                        numberOfErr = tmp[1];
                        break;
                    }
                    case "storeName":
                    {
                        storeName = tmp[1];
                        break;
                    }
                }
            }
        }
    }

    public SqlResultInfo reportSalingErr(String errOccuDate/*yyyy-mm-dd*/, 
            Boolean isMoreThanRecord, String pName, String numberOfErr,
            String responserName,String commnet, String reponserId, String storeName,
            ERR_TYPE type)
    {
        Employee submitor = this.itsSysManager_.getCurEmployee();
        String spec = this.serializeSaleErrReport(isMoreThanRecord, pName, numberOfErr,storeName);
        SqlResultInfo result = this.itsErrReportingTable_.NewEntry(spec, type.toString(),
                submitor.getId().toString(), submitor.getName(), commnet, errOccuDate, responserName, reponserId);
        
        if(result.isSucceed())
        {
            // report to StoreStock manager to update StoreStock
            String adNumber;
            if(isMoreThanRecord)
            {
                adNumber = numberOfErr;
            }
            else
            {
                adNumber = "-"+numberOfErr;
            }
            itsStoreStackManager_.updateStoreStock(storeName, pName, 
                    Integer.parseInt(numberOfErr), isMoreThanRecord);
        }
        return result;
    }
    
    private String serializeSaleErrReport(Boolean isMoreThanRecord, String pName
                ,String numberOfErr, String storeName)
    {
        return "isMoreThanRecord="+(isMoreThanRecord?"1":"0")+"|productName="
                +pName+"|numberOfErro="+numberOfErr+"|storeName="+storeName;
    }
    
    private void unserializeSaleErrReport(String inputString, Boolean isMoreThanRecord, 
            String pName, String numberOfErr, String storeName)
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
                        isMoreThanRecord = "1".equals(tmp[1]);
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
                    case "storeName":
                    {
                        storeName = tmp[1];
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
