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
    public String getRmQuantity(){return quantity_.getQuantity().toString();}
    public String getRmUnitName(){return quantity_.getUnit().getUnitName();}
}
