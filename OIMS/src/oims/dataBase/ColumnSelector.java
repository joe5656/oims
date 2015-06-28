/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Maps;

/**
 *
 * @author freda
 */
public class ColumnSelector {
    final private String tableName_;
    private Map<String, String> itsColumns_;
    private Iterator<Map.Entry<String, String>>  itsItor_;
    private Set<Map.Entry<String, String>>     itsEntrySet_;
    
    public void addColumn(String columnName, String value)
    {
        if(itsColumns_ == null)
        {
            itsColumns_ = Maps.newHashMap();
        }
        itsColumns_.put(columnName, value);
    }
    
    public void removeColumn(String columnName)
    {
        itsColumns_.remove(columnName);
    }
    
    public void empty()
    {
        itsColumns_.entrySet();
        itsEntrySet_ = null;
        itsItor_ = null;
    }
    
    public Boolean isEmpty()
    {
        return itsColumns_.isEmpty();
    }
    
    public String TableName()
    {
        return tableName_;
    }
    
    public void initItor()
    {
        itsEntrySet_ = itsColumns_.entrySet();
        itsItor_ = itsEntrySet_.iterator();
    }
    
    public Map.Entry<String,String> getNext()
    {
        if(this.itsItor_ == null || !this.itsItor_.hasNext() || this.itsEntrySet_ == null)
        {
            return null;
        }
        
        return itsItor_.next();
    }
    
    private ColumnSelector(){tableName_ = "";};
}
