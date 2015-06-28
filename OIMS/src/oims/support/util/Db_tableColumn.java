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
public class Db_tableColumn {
    String  columnName_;
    Boolean isPrimary_;
    Boolean isUnique_;
    Db_publicColumnAttribute columnAttribute_;
    Boolean isAutoIncreasmnet_;
    
    public Db_tableColumn(String columnName, Db_publicColumnAttribute attr, 
            Boolean primary, Boolean Unique, Boolean autoInc)
    {
        columnName_ = columnName;
        isPrimary_ = primary;
        isUnique_= Unique;
        columnAttribute_= attr;
        isAutoIncreasmnet_ = autoInc;
    }
    
    public String getName()
    {
        return columnName_;
    }
    
    public Boolean isPrimary()
    {
        return isPrimary_;
    }
    
    public Boolean isAutoIncreasmnet()
    {
        return isAutoIncreasmnet_;
    }
    
    public Boolean isUnique()
    {
        return isUnique_;
    }
    
    public Db_publicColumnAttribute.ATTRIBUTE_NAME getAttributeName()
    {
        return columnAttribute_.getAttributeName();
    }
    
    public Db_publicColumnAttribute getAttribute()
    {
        return columnAttribute_;
    }
}
