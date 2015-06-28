/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.*;
import oims.dataBase.Db_table.TableEntry;
import oims.dataBase.tables.SyncerTable;
import oims.support.util.Db_tableColumn;
/**
 *
 * @author freda
 */
public class Db_dataBase_MYSQL implements Db_dataBase{
    final private Map<Db_db, Connection> connections_;
    private Db_db curDb_;
    private Connection curConnection_;
    private String host_;
    private String user_;
    private String password_;
    private String port_;
    final private Db_columnTypeMapper_MYSQL itsAttributeMapper_;
    final private String name_;
    private DataBaseManager itsDbm_;
    
    public Db_dataBase_MYSQL()
    {
        connections_ = Maps.newHashMap();
        itsAttributeMapper_ = new Db_columnTypeMapper_MYSQL();
        name_ = "defaultMYSQLDataBase";
    }
    
    public Db_dataBase_MYSQL(String name)
    {
        connections_ = Maps.newHashMap();
        itsAttributeMapper_ = new Db_columnTypeMapper_MYSQL();
        name_ = name;
    }
    
    private void CurConnectionFailed()
    {
        if(curConnection_ == null)
        {
            return;
        }
        itsDbm_.dbBrokenNotify(this);
    }
    
    @Override
    public String getDataBaseName(){return name_;}
    
    @Override
    public void initDataBase(String user, String pw, String host, String port)
    {
        host_ = host;
        user_ = user;
        password_ = pw;
        port_ = port;
        
        /*close all connections when initializing*/
        this.close();
    }
    
    private Connection setupConnection(Db_db db, Boolean isNoDbConnection)
    {
        Connection tempCon = null;
        try
        {
            if(host_ != null && user_ != null && password_ != null)
            {
                String url = "jdbc:mysql://" + host_ + ":" + port_ + "/" + (isNoDbConnection?"":db.getName()) 
                        + "?user="+ user_ +"&password="+password_+"&useUnicode=true&characterEncoding=UTF8";
                
                Class.forName("com.mysql.jdbc.Driver");
                
                tempCon = DriverManager.getConnection(url);
                if(tempCon != null && !isNoDbConnection)
                {
                    this.curConnection_ = tempCon;
                    this.curDb_ = db;
                    this.connections_.put(db, tempCon);
                }
            }
        }catch(ClassNotFoundException | SQLException e){}
        
        return tempCon;
    }
    
    @Override
    public void setDbm(DataBaseManager dbm){itsDbm_ = dbm;}
    
    @Override
    public Db_db createDb(Db_db db)
    {
        String query = "CREATE DATABASE IF NOT EXISTS "+ db.getName() + " DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";   
        Db_db returnValue = null;
        try
        {
            if((returnValue = this.selectDb(db)) == null)
            {
                /*db not exist create tmp cn that used for creating Db
                  tmp cn will not be stored and should be closed
                */
                Connection tempCn = this.setupConnection(db, Boolean.TRUE);
                
                if(tempCn != null)
                {
                    try (Statement sm = tempCn.createStatement()) {
                        int result = sm.executeUpdate(query);
                        if(result != -1)
                        {
                            /*now we can create a connection with specified DB*/
                            Connection cnWithDb = setupConnection(db, Boolean.FALSE);
                            this.curConnection_ = cnWithDb;
                            this.curDb_ = db;
                            this.connections_.put(db, cnWithDb);
                            returnValue = db;
                        }
                    }
                    tempCn.close();
                }
            }
        }catch(Exception e){System.out.print(e);}
        return returnValue;
    };
    
    /**
     * create a tablbe in selected DB
     * @param table
     * @return DB in which table was created
     */
    @Override
    public Db_db createTable (Db_table table)
    {
        Db_db db = null;
        String query = "CREATE TABLE IF NOT EXISTS "+ table.getName() + " (";   
        String primaryKeyName = null;
        Set<Map.Entry<String, Db_tableColumn>> tableEntries;
        List<String> listOfUniqueKey = Lists.newArrayList();
        
        if(curConnection_ != null)
        {
            try
            {
                tableEntries = table.getEntrySet();
                for(Map.Entry<String, Db_tableColumn> entry: tableEntries)
                {
                    Db_tableColumn entryContent = entry.getValue();
                    
                    if(entryContent.isPrimary() && primaryKeyName == null)
                    {
                        primaryKeyName = entryContent.getName();
                    }
                    else if(entryContent.isPrimary() && primaryKeyName != null)
                    {
                        throw new NumberFormatException();
                    }
                    
                    if(itsAttributeMapper_ == null)
                    {
                        throw new NumberFormatException();
                    }
                    
                    if(entryContent.isUnique())
                    {
                        listOfUniqueKey.add(entryContent.getName());
                    }
                    
                    String mappedAttribute = this.itsAttributeMapper_.MapAttribute(entryContent.getAttributeName());
                    if(entryContent.getAttribute().isEnum())
                    {
                        mappedAttribute += entryContent.getAttribute().buildEnumValueListString();
                    }
                    query += entryContent.getName() + " " + mappedAttribute 
                            + (entryContent.isAutoIncreasmnet()?" AUTO_INCREMENT ":" ")
                            + (entryContent.isUnique()?" UNIQUE, ": ", ");
                }
                
                query += "PRIMARY KEY (" + primaryKeyName + "));";
                try (Statement sm = curConnection_.createStatement()) {
                    sm.executeUpdate(query);
                    db = this.curDb_;
                }
                
            }catch(NumberFormatException e){System.out.print(e);}
             catch(SQLException e){System.out.print(query);this.CurConnectionFailed();}
        }
        return db;
    };
    
    /**
     *close all connections and release the resource
     * @param entry
     * @param table
     * @return 
     */
    @Override
    public Boolean insertRecord(Db_table.TableEntry entry, Db_table table)
    {
        Boolean result = Boolean.FALSE;
        if(this.curConnection_ != null && entry != null && entry.containsNotNullEntry())
        {
            try{
                String[] buildSql = entry.buildColumnAndValueList();
                String query = "insert into " + table.getName() + buildSql[0]
                        + " values " + buildSql[1] + " ;";
                
                try (Statement sm = curConnection_.createStatement()) {
                    if(0 < sm.executeUpdate(query))
                    {
                        result = Boolean.TRUE;
                    }
                }
            }catch(SQLException e){this.CurConnectionFailed();}
        }
        return result;
    }
    
    /**
     *close all connections and release the resource
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void close()
    {
        curDb_ = null;
        curConnection_ = null;
        try
        {
            for(Map.Entry<Db_db, Connection> entry: connections_.entrySet())
            {
                entry.getValue().close();
            }
        }catch(Exception e){};
        
        connections_.clear();
    };
    
    /**
     * select DB to be used
     * @param db
     * @return
     */
    @Override
    @SuppressWarnings("empty-statement")
    public Db_db selectDb(Db_db db)
    {
        Db_db returnValue = null;
        if(curDb_ != null && (curDb_.getName() == null ? db.getName() == null : curDb_.getName().equals(db.getName())))
        {
            returnValue = curDb_;
        }
        else
        {
            try
            {
                Connection newCon;
                if((newCon = this.connections_.get(db))!=null)
                {
                    this.curDb_ = db;
                    this.curConnection_ = newCon;
                    returnValue = db;
                }
                else
                {
                    if(setupConnection(db, Boolean.FALSE) != null)
                    {
                        returnValue = db;
                    }
                }
            }catch(Exception e){};
        }
        
        return returnValue;
    }
    
    /**
     * look up for current DB connection stored in Db_database_MYSQL
     * @return Db_db
     */
    @Override
    public Db_db currentDb(){return curDb_;};
    
    @Override
    public ResultSet  select(List<String> selectList, TableEntry entry_eq,
            TableEntry entry_gr, TableEntry entry_sml, Db_table table)
    {
        ResultSet resultValue = null;
        if(this.curConnection_ != null && selectList != null)
        {
            try{
                String query = "select ";
                if(selectList.isEmpty())
                {
                    query = query + " count(*) ";
                }
                else
                {
                    for(Integer i=0; i<selectList.size();i++)
                    {
                        if(i != 0)
                        {
                            query = query + ", ";
                        }
                        query = query + selectList.get(i);
                    }
                }
                
                // from
                query = query + " from " + table.getName() + 
                        this.generateQuery_where(entry_eq, entry_gr, entry_sml);
                
                try (Statement sm = curConnection_.createStatement()) {
                    resultValue = sm.executeQuery(query);
                }
            }catch(SQLException e){this.CurConnectionFailed();}
        }
        return resultValue;
    };
    
    @Override
    public Boolean  update(Db_table.TableEntry updateEntry, Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml, Db_table table)
    {
        Boolean resultValue = Boolean.FALSE;
        if(this.curConnection_ != null && updateEntry != null && updateEntry.containsNotNullEntry())
        {
            try{
                String query = "update " + table.getName() + " set ";
                Set<Entry<String,String>> entrySet = updateEntry.getEntrySet();
                Iterator<Entry<String,String>> itr = entrySet.iterator();
                Boolean firstLoop = Boolean.TRUE;
                while(itr.hasNext())
                {
                    Entry<String,String> tmpEntry = itr.next();
                    if(tmpEntry.getValue() != null)
                    {
                        if(Objects.equals(firstLoop, Boolean.TRUE))
                        {
                            firstLoop = Boolean.FALSE;
                            query = query + tmpEntry.getKey() + "=" +
                                tmpEntry.getValue();
                        }
                        else
                        {
                            query = query + ", " + tmpEntry.getKey() + "=" +
                                tmpEntry.getValue();
                        }
                    }
                }
                
                // where
                query += this.generateQuery_where(entry_eq, entry_gr, entry_sml);
                
                try (Statement sm = curConnection_.createStatement()) {
                    if(0 <= sm.executeUpdate(query))
                    {
                        resultValue = Boolean.TRUE;
                    }
                }
            }catch(SQLException e){this.CurConnectionFailed();}
        }
        return resultValue;
    };
    
    @Override
    public Boolean delete(Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml, Db_table table)
    {
        Boolean resultValue = Boolean.FALSE;
        if(this.curConnection_ != null && 
                ((entry_gr != null && entry_gr.containsNotNullEntry()) ||
                (entry_eq != null && entry_eq.containsNotNullEntry()) || 
                (entry_sml != null && entry_sml.containsNotNullEntry())))
        {
            try{
                String query = "delete from " + table.getName() + " " + this.generateQuery_where(entry_eq, entry_gr, entry_sml);
                               
                try (Statement sm = curConnection_.createStatement()) {
                    if(0 <= sm.executeUpdate(query))
                    {
                        resultValue = Boolean.TRUE;
                    }
                }
            }catch(SQLException e){this.CurConnectionFailed();}
        }
        return resultValue;
    };
    
    public String generateQuery_where(Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml)
    {
        Boolean whereAdded = Boolean.FALSE;
        Boolean firstLoop = Boolean.TRUE;
        String query = "";
        
        // where
        if(entry_eq != null)
        {
            entry_eq.initItr();
            Entry<String, String> entry;
            while((entry = entry_eq.nexNotNullEntry()) != null)
            {
                if(firstLoop)
                {
                    firstLoop = Boolean.FALSE;
                    if(!whereAdded)
                    {
                        query = query + " where ";
                        whereAdded = Boolean.TRUE;
                    }
                }
                else
                {
                    query = query + " and ";
                }

                query = query + entry.getKey() + "=" + entry.getValue();
            }
        }

        if(entry_gr != null)
        {
            firstLoop = Boolean.TRUE;
            entry_gr.initItr();
            Entry<String, String> entry;
            while((entry = entry_gr.nexNotNullEntry()) != null)
            {
                if(firstLoop && !whereAdded)
                {
                    firstLoop = Boolean.FALSE;
                    if(!whereAdded)
                    {
                        query = query + " where ";
                        whereAdded = Boolean.TRUE;
                    }
                }
                else
                {
                    query = query + " and ";
                }

                query = query + entry.getKey() + ">" + entry.getValue();
            }
        }

        if(entry_sml != null)
        {
            firstLoop = Boolean.TRUE;
            entry_sml.initItr();
            Entry<String, String> entry;
            while((entry = entry_sml.nexNotNullEntry()) != null)
            {
                if(firstLoop && !whereAdded)
                {
                    firstLoop = Boolean.FALSE;
                    if(!whereAdded)
                    {
                        query = query + " where ";
                        whereAdded = Boolean.TRUE;
                    }
                }
                else
                {
                    query = query + " and ";
                }

                query = query + entry.getKey() + "<" + entry.getValue();
            }
        }
        return query;
    }

    @Override
    public Boolean testConnection() {        
        return null != this.setupConnection(null, Boolean.TRUE);
    }

    @Override
    public Boolean isDataBaseCreated(Db_db db) {
        return null != this.setupConnection(db, Boolean.FALSE);
    }

    @Override    
    public Boolean isDataBaseConectionAlive(int timeout) throws SQLException
    {
        Boolean retureValue = Boolean.FALSE;
        if(this.curConnection_ != null)
        {
            retureValue = this.curConnection_.isValid(timeout);
        }
        return retureValue;
    }
    
    public Boolean truncateTable(Db_table table)
    {
        Boolean result = Boolean.TRUE;
        if(this.curConnection_ == null)
        {
            return Boolean.FALSE;
        }
        
        String query = "TRUNCATE table " + table.getName();
        try (Statement sm = curConnection_.createStatement()) 
        {
            sm.executeUpdate(query);
        } catch (SQLException ex) {
            result = Boolean.FALSE;
            Logger.getLogger(Db_dataBase_MYSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public Boolean batchExecute(Set<String> queryBatch)
    {
        Boolean result = Boolean.TRUE;
        if(this.curConnection_ == null)
        {
            return Boolean.FALSE;
        }
        
        try (Statement sm = curConnection_.createStatement()) 
        {
            for(String str:queryBatch)
            {
                sm.addBatch(str);
            }
            int[] re = sm.executeBatch();
            for(int i:re)
            {
                if(re[i] < 0)
                {
                    result = Boolean.FALSE;
                    break;
                }
            }
        } catch (SQLException ex) {
            result = Boolean.FALSE;
            Logger.getLogger(Db_dataBase_MYSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    @Override
    public Boolean copyTable(Db_dataBase cpyTo, Db_table copyTable) 
    {
        Boolean result = Boolean.FALSE;
        if(cpyTo.getType() != db_type.MYSQL || this.curConnection_ == null)
        {
            return result;
        }
        Db_dataBase_MYSQL mysqlDataBase = (Db_dataBase_MYSQL)cpyTo;
        
        // Make a direct copy from this DataBase to dst dataBase
        String query = "select * from " + copyTable.getName();
        try (Statement sm = curConnection_.createStatement()) {
            ResultSet resultSet = sm.executeQuery(query);
            if(resultSet != null)
            {
                if(mysqlDataBase.truncateTable(copyTable))
                {
                    ResultSetMetaData data = resultSet.getMetaData();
                    int colCnt = data.getColumnCount();
                    String coln = " (";
                    for(int i = 0;i< colCnt;i++)
                    {
                        coln += " " + data.getColumnName(i)+", ";
                    }
                    coln += ") ";
                    resultSet.first();
                    Set<String> querySet = Sets.newConcurrentHashSet();
                    do
                    {
                        String buildSql = " (";
                        for(int i = 0;i< colCnt;i++)
                        {
                            buildSql += " " + resultSet.getString(i)+", ";
                        }
                        buildSql += " )";
                        query = "insert into " + copyTable.getName() + coln
                        + " values " + buildSql + " ;";
                        querySet.add(buildSql);
                    }while(resultSet.next());
                    if(mysqlDataBase.batchExecute(querySet))
                    {
                        result = Boolean.TRUE;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Db_dataBase_MYSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public db_type getType() {return db_type.MYSQL;}

    @Override
    public ResultSet getSyncTable() 
    {
        ResultSet rs = null;
        if(this.curConnection_ != null)
        {
            Statement sm;
            try {
                sm = curConnection_.createStatement();
                rs = sm.executeQuery("select * from "+SyncerTable.getDerivedTableName());
            } catch (SQLException ex) {
                Logger.getLogger(Db_dataBase_MYSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rs;
    }
}
