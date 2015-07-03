/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.rawMaterialManagement;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.tables.RawMaterialTable;
import oims.support.util.CommonUnit;
import oims.warehouseManagemnet.WareHouse;

/**
 *
 * @author freda
 */
public class RawMaterial {
    CommonUnit pricingUnit_;
    Integer id_;
    String  name_;
    Double  normalPrice_;
    Boolean isvalid_;
    
    private RawMaterial(){};
    public RawMaterial(Integer id)
    {
        id_ = id;
        name_ = "noname";
        normalPrice_ = 0.0;
        isvalid_ = Boolean.FALSE;
    }

    public Integer    getId(){return id_;}
    public String     getName(){return name_;}
    public Boolean    isValid(){return isvalid_;}
    public CommonUnit getPricingUnit(){return pricingUnit_;}
    public Double     getNormalPrice(){return normalPrice_;}
    
    public void setId(Integer id){id_ = id;}
    public void setName(String name){name_ = name;}
    public void setValid(Boolean v){isvalid_ = v;}
    public void setPricingUnit(CommonUnit u){pricingUnit_ = u;}
    public void setNormaiPrice(Double price){normalPrice_ = price;}

}
