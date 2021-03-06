/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.producingPlanManagement;

import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Map;
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
    
    public Boolean planLocked(Date planDate, String storeName)
    {
        Boolean result = false;
        if(planDate != null && storeName != null)
        {
            result = this.itsProductPlanTable_.isPlanLocked(planDate, storeName);
        }
        return result;
    }
    
    public Boolean planExisted(Date planDate, String storeName)
    {
        Boolean result = false;
        if(planDate != null && storeName != null)
        {
            result = this.itsProductPlanTable_.isPlanExisted(planDate, storeName);
        }
        return result;
    }
    
    public SqlResultInfo plan(Date planDate, String storeName, ProductPlanDataTable detail,
            String planer, String planerId)
    {
        SqlResultInfo result = new SqlResultInfo(false);
        
        if(planDate != null && storeName != null)
        {
            if(planExisted(planDate, storeName))
            {
                result = this.updatePlan(planDate, storeName, detail);
            }
            else
            {
                result = this.newPlan(planDate, storeName, detail, planer, planerId);
            }
        }
        return result;
    }
    
    private SqlResultInfo updatePlan(Date planDate, String storeName, ProductPlanDataTable detail)
    {
        if(planDate == null || storeName == null || detail == null || this.planLocked(planDate, storeName))
        {
            return new SqlResultInfo(false);
        }
        return this.itsProductPlanTable_.update(storeName, planDate, detail, false, null, null);
    }
    
    private SqlResultInfo newPlan(Date planDate, String storeName, ProductPlanDataTable detail,
            String planer, String planerId)
    {
        if(planDate == null || storeName == null || detail == null)
        {
            return new SqlResultInfo(false);
        }
        return this.itsProductPlanTable_.newPlan(planDate, detail, planerId, planer, storeName);
    }
    
    public ProductPlanDataTable getPlan(String storeName, Date planDate)
    {
        ProductPlanDataTable result = new ProductPlanDataTable(this.itsProductPlanTable_.getStorePlanInString(storeName, planDate.toString()));
        return result;
    }
}
