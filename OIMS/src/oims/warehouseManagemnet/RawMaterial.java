/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.warehouseManagemnet;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.tables.RawMaterialTable;
import oims.support.util.CommonUnit;

/**
 *
 * @author freda
 */
public class RawMaterial {
    CommonUnit pricingUnit_;
    Integer id_;
    Boolean syncd_;
    String  name_;
    Double  normalPrice_;
    Boolean isvalid_;
    RawMaterialTable itsRawMaterialTable_;
    
    private RawMaterial(){};
    public RawMaterial(Integer id, RawMaterialTable tb)
    {
        id_ = id;
        syncd_ = Boolean.FALSE;
        name_ = "noname";
        normalPrice_ = 0.0;
        isvalid_ = Boolean.FALSE;
        itsRawMaterialTable_ = tb;
    }
    
    public RawMaterial(String name, RawMaterialTable tb)
    {
        id_ = -1;
        syncd_ = Boolean.FALSE;
        name_ = name;
        normalPrice_ = 0.0;
        isvalid_ = Boolean.FALSE;
        itsRawMaterialTable_ = tb;
    }
    
    public Boolean    isSyncd(){return syncd_;}
    public Integer    getId(){return id_;}
    public String     getName(){return name_;}
    public Boolean    isValid(){return isvalid_;}
    public CommonUnit getPricingUnit(){return pricingUnit_;}
    public Double     getNormalPrice(){return normalPrice_;}
    
    public void setSyncd(){syncd_ = Boolean.TRUE;}
    public void setId(Integer id){id_ = id;}
    public void setName(String name){name_ = name;}
    public void setValid(Boolean v){isvalid_ = v;}
    public void setPricingUnit(CommonUnit u){pricingUnit_ = u;}
    public void setNormaiPrice(Double price){normalPrice_ = price;}
    
    public void serialize()
    {
        try {
            itsRawMaterialTable_.serializeRawmaterialInstance(this);
        } catch (SQLException ex) {
            Logger.getLogger(WareHouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disable()
    {
        if(!isSyncd())
        {
            serialize();
        }
        
        if(isValid())
        {
            this.itsRawMaterialTable_.disableRawMaterial(this);
        }
    }
    
    public void enable()
    {
        if(!isSyncd())
        {
            serialize();
        }
        
        if(!isValid())
        {
            this.itsRawMaterialTable_.enableRawMaterial(this);
        }
    }
}
