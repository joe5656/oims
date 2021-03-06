/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.taskDistributor;

import com.google.common.collect.Maps;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import oims.employeeManager.EmployeeManager;
import oims.producingPlanManagement.ProducingPlan;
import oims.producingPlanManagement.ProducingPlanManager;
import oims.reciptManagement.ReciptManager;
import oims.support.util.ProductPlanDataTable;
import oims.ticketSystem.TicketManager;

/**
 *
 * @author ezouyyi
 */
public class TaskDistributor {
    private TicketManager itsTicketManager_;
    private ProducingPlanManager itsProducingPlanManager_;
    private ReciptManager itsReciptManager_;
    
    public void actionWhenPlanLocked(ProducingPlan plan)
    {
        /*Step0 check plan date, if it's out of date do nothing*/
        if(plan.getPlanDate().before(new Date(System.currentTimeMillis())))
        {
            return;
        }
        
        /*Step1 Generate CO ticket*/
        Integer submitorId = EmployeeManager.getRobetId();
        String  submitorName = EmployeeManager.getRobetName();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(plan.getPlanDate());
        calendar.set(Calendar.DATE, Calendar.DATE - 1);
        // CK request day should be one day ahead plan date
        Date CkReqeustDate = calendar.getTime();
        Map<String, String> planData = plan.getPlanData().getProductList();
        for(Entry<String, String> entry:planData.entrySet())
        {
            String recieverName = entry.getKey();
            Integer recieverId = 22; // TODO: get ID
            
        }
        
        
        /*Step2 update stock: stock updating will add future quantity into
          current stock. So special processing will be needed when query current
          stock information(like take away future quantity from stock to show real
          current number)*/
        
    }
    
    public void genReceivingList(Date forDate, Boolean isWarehouse, Integer StoreWhId)
    {
        
    }
    
    public Map<String, Double> showProductToreciptList(String storeName, Date planDate)
    {
        Map<String, Double> result = Maps.newHashMap();
        ProductPlanDataTable plan = this.itsProducingPlanManager_.getPlan(storeName, planDate);
        if(plan.productNum() > 0 && plan.initItr())
        {
            result = this.itsReciptManager_.calDetailReciptList(plan);
        }
        
        return result;
    }
}
