/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.producingPlanManagement;

import java.util.Date;
import java.util.Map;
import oims.support.util.ProductPlanDataTable;

/**
 *
 * @author ezouyyi
 */
public class ProducingPlan {
    private Boolean isLocked_;
    private String  locker_;
    private Integer lockerId_;
    private String  planner_;
    private Integer plannerId_;
    private Date    lockedAt_;
    private Date    planDate_;
    private ProductPlanDataTable plan_;
    
    public Date getPlanDate(){return planDate_;}
    public ProductPlanDataTable getPlanData(){return plan_;}
}
