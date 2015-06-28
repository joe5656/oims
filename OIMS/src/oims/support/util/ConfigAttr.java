/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

/**
 *
 * @author freda
 */
public class ConfigAttr {
    String type_;
    String value_;
    
    ConfigAttr(String type, String value)
    {
        type_ = type;
        value_ = value;
    }
    
    public String getType(){return type_;}
    public String getValue(){return value_;}
}
