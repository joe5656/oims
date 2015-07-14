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
    private final String rmId_;
    private final UnitQuantity quantity_;
    
    public QuantitiedRawMaterial( String id, String unit, String q,String name)
    {
        rmName_ = name;
        rmId_ = id;
        quantity_ = new UnitQuantity(new CommonUnit(unit),Double.parseDouble(q));
    }
    
    
    public String getRmName(){return rmName_;}
    public String getRmId(){return rmId_;}
    public String getRmQuantity(){return quantity_.getQuantity().toString();}
    public String getRmUnitName(){return quantity_.getUnit().getUnitName();}
}
