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
public class QuantitiedRawMaterial {
    private final String rmName_;
    private final UnitQuantity quantity_;
    
    public QuantitiedRawMaterial(String unit, String q,String name)
    {
        rmName_ = name;
        quantity_ = new UnitQuantity(new CommonUnit(unit),Double.parseDouble(q));
    }
    
    
    public String getRmName(){return rmName_;}
    public UnitQuantity getUnitQuantity(){return quantity_;}
    public String getRmQuantity(){return quantity_.getQuantity().toString();}
    public String getRmUnitName(){return quantity_.getUnit().getUnitName();}
    public Boolean add(QuantitiedRawMaterial add)
    {
        Boolean result = false;
        if(add.getRmName().equals(rmName_))
        {
            UnitQuantity addQ = add.getUnitQuantity();
            result = quantity_.add(addQ);
        }
        return result;
    }
    
    public void multiply(Double factor)
    {
        quantity_.multiply(factor);
    }
    
}
