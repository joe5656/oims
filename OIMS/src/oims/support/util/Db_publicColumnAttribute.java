/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import java.util.Set;

/**
 *
 * @author ezouyyi
 */
public class Db_publicColumnAttribute {
    private final ATTRIBUTE_NAME attributeName_;
    private final String[] enumValues_;
    
    public  Boolean isEnum()
    {
        return attributeName_ == ATTRIBUTE_NAME.ENUM;
    }
    
    public  ATTRIBUTE_NAME getAttributeName()
    {
        return attributeName_;
    }
    
    public String buildEnumValueListString()
    {
        String returnString = "";
        if(attributeName_ == ATTRIBUTE_NAME.ENUM)
        {
            returnString += "(";
            for(Integer i = 0; i < enumValues_.length; i++)
            {
                returnString += enumValues_[i] + ":";
            }
            returnString += ")";
        }
        return returnString;
    }
    
    public Db_publicColumnAttribute(ATTRIBUTE_NAME attribute, String[] enValue)
    {
        attributeName_ = attribute;
        enumValues_ = enValue;
    }
    
    public enum ATTRIBUTE_NAME 
    {
        BIT,
        SMALL_INTEGER,
        INTEGER,
        DOUBLE,
        FLOAT,
        DATE,
        TIME,
        TIMESTAMP,
        DATATIME,
        YEAR,
        VARCHAR10,
        VARCHAR60,
        TEXT,
        LONG_TEXT,
        ENUM,
        NUMBER_OF_ATTRIBUTE_NAME
    }
}
