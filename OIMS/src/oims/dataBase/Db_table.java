/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import oims.support.util.Db_publicColumnAttribute;
import oims.support.util.Db_publicColumnAttribute.ATTRIBUTE_NAME;
import oims.support.util.Db_tableColumn;
import oims.support.util.SqlResultInfo;
/**
 *
 * @author freda
 */
public class Db_table {
    private String tableName_;
    private Map<String, Db_tableColumn> columns_;
    final private DataBaseManager itsDataBaseManager_;
    final private Table_Type dbType_;
    
    public enum Table_Type
    {
        TABLE_TYPE_REMOTE,  // remote table only existed on server
        TABLE_TYPE_2_LEVEL, // 2-level table keep temparay un-synced data locally
        TABLE_TYPE_MIRROR   // mirrow table keep mirrow locally and should be synced regularly
    }
    
    public Db_table(String name, DataBaseManager dbm, Table_Type type)
    {
        tableName_ = name;
        columns_ = Maps.newHashMap();
        itsDataBaseManager_ = dbm;
        dbType_ = type;
    }
                
    public class TableEntry{
        private Map<String, String> entryContent_;
        private String tableName_;
        private Iterator<Entry<String, String>> Itr_;  
        
        public TableEntry(String tableName)
        {
            tableName_ = tableName;
            entryContent_ = Maps.newHashMap();
        }
        
        public Set<Entry<String,String>> getEntrySet()
        {
            return this.entryContent_.entrySet();
        }
        
        public Boolean containsNotNullEntry()
        {
            Boolean returnValue = Boolean.FALSE;
            Set<Map.Entry<String, String>> entrySet = entryContent_.entrySet();
            Iterator<Entry<String, String>> itr = entrySet.iterator();
            while(itr.hasNext())
            {
                Entry<String, String> tmpEntry = itr.next();
                if(tmpEntry.getValue() != null)
                {
                    returnValue = Boolean.TRUE;
                    break;
                }
            }
            return returnValue;
        }
        public void generateSelectList(List<String> selectList)
        {
            Set<Map.Entry<String, String>> entrySet = entryContent_.entrySet();
            Iterator<Entry<String, String>> itr = entrySet.iterator();
            while(itr.hasNext())
            {
                Entry<String, String> tmpEntry = itr.next();
                if(tmpEntry.getValue() != null)
                {
                    selectList.add(tmpEntry.getKey());
                }
            }
        }
        
        public void initItr()
        {
            Itr_ = entryContent_.entrySet().iterator(); 
        }
        
        public Entry<String, String> nexNotNullEntry()
        {
            Entry<String, String> returnValue = null;
            if(Itr_.hasNext())
            {
                returnValue = Itr_.next();
                if(returnValue.getValue() == null)
                {
                    returnValue = this.nexNotNullEntry();
                }
            }
            return returnValue;
        }
        
        public String[] buildColumnAndValueList()
        {
            String returnvalue[] = new String[2];
            String clist = " (";
            String vlist = " (";
            Integer loop = 0;
            for(Map.Entry<String, String> entry: entryContent_.entrySet()) {
                loop++;
                if(loop < entryContent_.size())
                {
                    clist += entry.getKey()+",";
                    vlist += (entry.getValue() == null?"null":"'"+entry.getValue()+"'")+",";
                }
                else
                {
                    clist += entry.getKey();
                    vlist += (entry.getValue() == null?"null":"'"+entry.getValue()+"'");
                }
                
                
            }
            clist += ") ";
            vlist += ") ";
            
            returnvalue[0] = clist;
            returnvalue[1] = vlist;
            return returnvalue; 
        }
        
        /**
         * DO NOT CALL THIS FUNCTION DIRECTLY OUTSIDE Db_table
         * @param columns_ 
         */
        public void registerEntryContent(Map<String, Db_tableColumn> columns_)
        {
            columns_.entrySet().stream().forEach((entry) -> {
                entryContent_.put(entry.getKey(), null);
            });
        }
        
        public String getEntryValue(String ColumnName)
        {
            return entryContent_.get(ColumnName);
        }
        
        public Boolean fillInEntryValues(Map<String, String> values)
        {
            Boolean result = Boolean.TRUE;
            for(Map.Entry<String, String> entry: values.entrySet())
            {
                if(entryContent_.containsKey(entry.getKey()))
                {
                    entryContent_.replace(entry.getKey(), entry.getValue());
                }
                else
                {
                    result = Boolean.FALSE;
                    break;
                }
            }
            return result;
        }
    }
    
    public TableEntry generateTableEntry()
    {
        TableEntry tmpEntry = new TableEntry(this.tableName_);
        tmpEntry.registerEntryContent(columns_);
        return tmpEntry;
    }

    public Set<Map.Entry<String, Db_tableColumn>> getEntrySet()
    {
        return columns_.entrySet();
    }
    
    /* function to create column attributes
    */
    public void registerColumn(String columnName, ATTRIBUTE_NAME attr, 
            Boolean primary, Boolean Unique, Boolean autoInc, String paras)
    {
        String[] enValues = null;
        if(attr == ATTRIBUTE_NAME.ENUM)
        {
            /*when carrying enum members, para should be in the format of:
            value1:value2:value3
            */
            enValues = paras.split(":");
        }
        
        Db_publicColumnAttribute newAtr = new Db_publicColumnAttribute(attr,enValues);
        Db_tableColumn col = new Db_tableColumn(columnName, newAtr, primary, Unique, autoInc);
        columns_.put(columnName, col);
    }
    
    public Table_Type getTableType(){return dbType_;}
    public String getName(){return tableName_;}
    protected SqlResultInfo insertRecord(TableEntry enryToBeInsert)
    {
        SqlResultInfo result = new SqlResultInfo(Boolean.FALSE);
        DbmReqHandler handler = this.itsDataBaseManager_.insertReq(this);
        if(handler == null)
        {
            result.setErrInfo("无法创建数据库连接请求，位置Db_table.insertRecord");
        }
        
        handler.fillInData(enryToBeInsert);
        return handler.execute();
    }
    
    protected SqlResultInfo select(TableEntry entry_select, TableEntry entry_equal, TableEntry entry_gr, TableEntry entry_sml)
    {
        DbmReqHandler handler = this.itsDataBaseManager_.selectReq(this);
        if(handler == null)
        {
            return null;
        }
        handler.fillInData(entry_select);
        handler.fillInEqData(entry_equal);
        handler.fillInGrData(entry_gr);
        handler.fillInSmlData(entry_sml);
        return handler.execute();
    }
    
    protected SqlResultInfo update(TableEntry entry_update, TableEntry entry_equal, 
            TableEntry entry_gr, TableEntry entry_sml)
    {
         DbmReqHandler handler = this.itsDataBaseManager_.updateReq(this);
        if(handler == null)
        {
            return null;
        }
        
        handler.fillInData(entry_update);
        handler.fillInEqData(entry_equal);
        handler.fillInGrData(entry_gr);
        handler.fillInSmlData(entry_sml);
        return handler.execute();       
    }
    
    protected SqlResultInfo delete(TableEntry entry_equal, 
            TableEntry entry_gr, TableEntry entry_sml)
    {
         DbmReqHandler handler = this.itsDataBaseManager_.deleteReq(this);
        if(handler == null)
        {
            return null;
        }
        
        handler.fillInEqData(entry_equal);
        handler.fillInGrData(entry_gr);
        handler.fillInSmlData(entry_sml);
        return handler.execute();          
    }
    
    public Boolean registerToDbm()
    {
        return this.itsDataBaseManager_.registerTable(this);
    }
    
    public Vector getColumns()
    {
        Set<String> names = this.columns_.keySet();
        Vector returnValue = new Vector();
        for(String str:names)
        {
            returnValue.addElement(str);
        }
        return returnValue;
    }
    
    public void translateColumnName(Vector col){}
    public Boolean uploadL2CachedData(){return Boolean.TRUE;}
}
