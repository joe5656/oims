/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.producingPlanManagement;

import java.util.Date;
import oims.dataBase.tables.ProducingPlanTable;
import oims.support.util.ProductPlanDataTable;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class ProducingPlanManager  implements oims.systemManagement.Client{
    private SystemManager itsSysManager_;
    private ProducingPlanTable itsProductPlanTable_;

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
                itsProductPlanTable_.registerToDbm();
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
    public void setSystemManager(SystemManager sysManager) {itsSysManager_ = sysManager;}
    
    public SqlDataTable getStorePlan(String storeName, String date)
    {
        SqlDataTable result = this.itsProductPlanTable_.getStorePlan(storeName, date);
        this.itsProductPlanTable_.translateColumnName(result.getColumnNames());
        return result;
    }
    
    public SqlResultInfo lockPlan(Date planDate, String storeName, String lockerId, 
            String lockerName)
    {
        if(planDate == null)return new SqlResultInfo(false);
        return this.itsProductPlanTable_.update(storeName, planDate, null, Boolean.TRUE, lockerId, lockerName);
    }
    
    public SqlResultInfo updatePlan(Date planDate, String storeName, ProductPlanDataTable detail)
    {
        if(planDate == null || storeName == null || detail == null)
        {
            return new SqlResultInfo(false);
        }
        return this.itsProductPlanTable_.update(storeName, planDate, detail, false, null, null);
    }
    
    public SqlResultInfo newPlan(Date planDate, String storeName, ProductPlanDataTable detail,
            String planer, String planerId)
    {
        if(planDate == null || storeName == null || detail == null)
        {
            return new SqlResultInfo(false);
        }
        return this.itsProductPlanTable_.newPlan(planDate, detail, planerId, planer, storeName);
    }
}
