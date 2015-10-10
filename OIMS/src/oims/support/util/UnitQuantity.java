/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

/**
 *
 * @author ezouyyi
 */
public class UnitQuantity {
    private CommonUnit unit_;
    private Double     quantity_;
    
    public UnitQuantity()
    {
        quantity_ = null;
        unit_  = null;
    }
    
    public UnitQuantity(CommonUnit unit, Double quantity)
    {
        unit_ = unit;
        quantity_ = quantity;
    }
    
    public UnitQuantity(String unit, Double quantity)
    {
        unit_ = new CommonUnit(unit);
        quantity_ = quantity;
    }
    
    public CommonUnit getUnit(){return unit_;}
    public Double     getQuantity(){return quantity_;}
    
    public Double     getQuantityInUnit(CommonUnit unit)
    {
        return quantity_*unit.getUnitChanageFactor(unit_.getUnit());
    }
    
    public Boolean greaterThan(UnitQuantity rh)
    {
        return quantity_ > rh.getQuantity()*rh.getUnit().getUnitChanageFactor(unit_.getUnit());
    }
    
    public Boolean equals(UnitQuantity rh)
    {
        return quantity_ == rh.getQuantity()*rh.getUnit().getUnitChanageFactor(unit_.getUnit());
    }
    
    public Boolean smallerThan(UnitQuantity rh)
    {
        return quantity_ < rh.getQuantity()*rh.getUnit().getUnitChanageFactor(unit_.getUnit());
    }
    
    public Boolean add(UnitQuantity addto)
    {
        Boolean result = false;
        double factor = addto.getUnit().getUnitChanageFactor(unit_.getUnit());
        if(factor != -1.0)
        {
            quantity_ += addto.getQuantity()*factor;
        }
        return result;
    }
    
    public void multiply(Double factor){this.quantity_ *= factor;}
}
