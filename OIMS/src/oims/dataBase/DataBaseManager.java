/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;
import oims.dataBase.syncer.DataBaseSyncer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.Tasks.DataBaseKeepAlive;
import oims.dataBase.Db_dataBase.db_type;
import oims.dataBase.Interfaces.dbStatus.DataBaseRx_dbStatus;
import oims.dataBase.Interfaces.dbStatus.DataBaseTx_dbStatus;
import oims.dataBase.tables.SyncerTable;
import oims.systemManagement.SystemManager;
import oims.systemManagement.SystemManager.systemStatus;
/**
 *
 * @author ezouyyi
 */
public class DataBaseManager  implements oims.systemManagement.Client,DataBaseRx_dbStatus{
    private Db_dataBase    localDataBase_;
    private Db_dataBase    remoteDataBase_;
    private Boolean        remoteDataBaseConnected_;
    private Boolean        localDataBaseConnected_;
    private Db_config      itsCnf_;
    private SystemManager  itsSysManager_;
    private Db_db          itsMysqlDb_;
    private DataBaseSyncer itsSyncer_;
    private SyncerTable    itsSyncerTable_;
    private List<DataBaseTx_dbStatus> itsDbStatusListeners_;
    private Map<String, Db_table>     itsMirrorTables_;
    private Map<String, Db_table>     itsRemoteTables_;
    private Map<String, Db_table>     itsL2Tables_;
    
    public DataBaseManager()
    {
        
    }
    @Override
    public Boolean systemStatusChangeNotify(systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSyncerTable_ = new SyncerTable(this);
                itsSyncer_ = new DataBaseSyncer(this, this.itsSyncerTable_);
                itsDbStatusListeners_ = Lists.newArrayList();
                remoteDataBaseConnected_ = Boolean.FALSE;
                localDataBaseConnected_ = Boolean.FALSE;
                itsCnf_ = new Db_config();
                itsMysqlDb_ = DataBaseFactory.newDb(Db_dataBase.db_type.MYSQL, 
                        itsCnf_.getDbName());
                itsMirrorTables_ = Maps.newHashMap();
                itsRemoteTables_ = Maps.newHashMap();
                itsL2Tables_ = Maps.newHashMap();
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "dbm");
                break;
            }
            case SYS_REGISTER:
            {
                this.registerTable(itsSyncerTable_);
            }
            case SYS_CONFIG:
            {
                initDataBases();
                /* try local DB connect if it's not connected abort
                   leave remote DB connection to other thread after system 
                   started
                */
                //localDataBaseConnected_ = localDataBase_.testConnection();
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "dbm");
                break;
            }
            case INSTALL_DB:
            {
                /** in this case means DB never created,
                 a install phase will be followed to install tables
                 so DB should first be created and DB service should start here*/
                this.createDbReq(itsMysqlDb_, Boolean.TRUE);
                startDatabaseService(Boolean.TRUE);
                break;
            }
            case INSTALL:
            {
                this.createTable(Boolean.TRUE);
                this.itsSyncer_.installEntry(this.itsMirrorTables_.keySet());
                break;
            }
            case SYS_START:
            {
                if(!localDataBaseConnected_)
                {
                    startDatabaseService(Boolean.TRUE);
                }
                
                Timer Localtimer = new Timer();
                Localtimer.schedule(new DataBaseKeepAlive(Localtimer, this, this.localDataBase_),10000, 30000);
                
                Timer remoteTimer = new Timer();
                remoteTimer.schedule(new DataBaseKeepAlive(remoteTimer, this, this.remoteDataBase_),10000, 15000);
                
                // start syncer
                this.itsSyncer_.start();
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "dbm");
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "dbm");
                break;
            }
        }
        return Boolean.TRUE;
    }
    
    public Boolean isRemoteDbConnected(){return remoteDataBaseConnected_;}
    public Boolean islocalDbConnected(){return localDataBaseConnected_;}
    public void    inforSycnerTableUpdated(Db_table table){this.itsSyncer_.tableUpdated(table);}
    @Override
    public void setSystemManager(SystemManager sysManager)
    {
        itsSysManager_ = sysManager;
    }

    @Override
    public Boolean registerListener(DataBaseTx_dbStatus listener) {
        Boolean returnValue = Boolean.FALSE;
        if(this.itsDbStatusListeners_.add(listener))
        {
            listener.SetRx(this);
            returnValue = Boolean.TRUE;
        }
        return returnValue;
    }
    
    public enum QUERY_TYPE
    {
        INSERT,
        SELECT,
        UPDATE,
        DELETE
    }
    
    private class Db_config{
        //Map<DatabaseName, Map<{user,value}{url,value}{pw,value}{port,value}}>>
        private Map<String, Map<String,String>> mysqlPool_; 
        final private String dbName_;
        
        Db_config()
        {
            mysqlPool_ = Maps.newHashMap();
            //TODO remove this line when oam implemented
            oam_configNotify();
            dbName_ = "OIMS"; //  DONOT change this name
        }
        
        public void oam_configNotify()
        {
            //TODO change this function as interface with config module
            Map<String,String> cfg = registerMysqlConfigKey();
            mysqlPool_.put("localDataBase", cfg);
            cfg.replace("url", "localhost");
            cfg.replace("user", "root");
            cfg.replace("pw", "Badytoy56");
            cfg.replace("port", "3306");
            
            cfg = registerMysqlConfigKey();
            mysqlPool_.put("remoteDataBase", cfg);
            cfg.replace("url", "192.168.1.107");
            cfg.replace("user", "tester");
            cfg.replace("pw", "Badytoy56");
            cfg.replace("port", "3306");
        }
        
        private Map<String,String> registerMysqlConfigKey()
        {
            Map<String,String> config = Maps.newHashMap();
            config.put("url", null);
            config.put("pw", null);
            config.put("user", null);
            config.put("port", null);
            return config;
        }
        
        public Set<String> getMysqlPoolNames()
        {
            return mysqlPool_.keySet();
        }
        
        public String getPoolConfig_url(String poolName)
        {
            String cfg = null;
            if(this.mysqlPool_.containsKey(poolName))
            {
                Map<String,String> poolcfg = mysqlPool_.get(poolName);
                cfg = poolcfg.get("url");
            }
            return cfg;
        }
        
        public String getPoolConfig_port(String poolName)
        {
            String cfg = null;
            if(this.mysqlPool_.containsKey(poolName))
            {
                Map<String,String> poolcfg = mysqlPool_.get(poolName);
                cfg = poolcfg.get("port");
            }
            return cfg;
        }
        
        public String getPoolConfig_pw(String poolName)
        {
            String cfg = null;
            if(this.mysqlPool_.containsKey(poolName))
            {
                Map<String,String> poolcfg = mysqlPool_.get(poolName);
                cfg = poolcfg.get("pw");
            }
            return cfg;
        }
        
        public String getPoolConfig_user(String poolName)
        {
            String cfg = null;
            if(this.mysqlPool_.containsKey(poolName))
            {
                Map<String,String> poolcfg = mysqlPool_.get(poolName);
                cfg = poolcfg.get("user");
            }
            return cfg;
        }
        
        public String getDbName()
        {
            return dbName_;
        }
    }
    
    private void startDatabaseService(Boolean localService)
    {
        if(Objects.equals(localService, Boolean.FALSE) && !remoteDataBaseConnected_)
        {
            remoteDataBaseConnected_ = remoteDataBase_.selectDb(itsMysqlDb_)!= null;
            this.notifyDbStatusListeners(remoteDataBase_, remoteDataBaseConnected_);
        }
        else if(!localDataBaseConnected_)
        {
            localDataBaseConnected_  = localDataBase_.selectDb(itsMysqlDb_)!= null;
            this.notifyDbStatusListeners(localDataBase_, localDataBaseConnected_);
        }
    }
    
    private void initDataBases()
    {
        // remote dataBase
        String dataBaseName = "remoteDataBase";
        Db_dataBase rdataBase = DataBaseFactory.newDataBase(Db_dataBase.db_type.MYSQL, dataBaseName);
        rdataBase.initDataBase(itsCnf_.getPoolConfig_user(dataBaseName), 
                    itsCnf_.getPoolConfig_pw(dataBaseName),
                    itsCnf_.getPoolConfig_url(dataBaseName),
                    itsCnf_.getPoolConfig_port(dataBaseName));
        remoteDataBase_ = rdataBase;
        remoteDataBase_.setDbm(this);
        
        // remote dataBase
        dataBaseName = "localDataBase";
        Db_dataBase ldataBase = DataBaseFactory.newDataBase(Db_dataBase.db_type.MYSQL, dataBaseName);
        ldataBase.initDataBase(itsCnf_.getPoolConfig_user(dataBaseName), 
                    itsCnf_.getPoolConfig_pw(dataBaseName),
                    itsCnf_.getPoolConfig_url(dataBaseName),
                    itsCnf_.getPoolConfig_port(dataBaseName));
        localDataBase_ = ldataBase;
        remoteDataBase_.setDbm(this);
    }
    
    public Boolean registerTable(Db_table table)
    {
        Boolean result = Boolean.FALSE;
        switch(table.getTableType())
        {
            case TABLE_TYPE_REMOTE:
            {
                result = (itsRemoteTables_.put(table.getName(), table) != null);
                break;
            }
            case TABLE_TYPE_2_LEVEL:
            {
                result = (itsL2Tables_.put(table.getName(), table) != null);
                break;
            }
            case TABLE_TYPE_MIRROR:
            {
                result = (itsMirrorTables_.put(table.getName(), table) != null);
                break;
            }
            default:{};
        }
        return result;
    }
    
    public void dbBrokenNotify(Db_dataBase failedDb)
    {
        if(failedDb != null)
        {
            switch(failedDb.getDataBaseName())
            {
                case "remoteDataBase":
                {
                    remoteDbMaintain();
                    break;
                }
                case "localDataBase":
                {
                    localDbMaintain();
                    break;
                }
            }
        }
    }
    
    private void notifyDbStatusListeners(Db_dataBase db, Boolean ok)
    {
        if(db == null)
        {
            return;
        }
        
        Iterator<DataBaseTx_dbStatus> itr = this.itsDbStatusListeners_.iterator();
        while(itr.hasNext())
        {
            DataBaseTx_dbStatus listener = itr.next();
            if(Objects.equals(ok, Boolean.FALSE))
            {
                if("localDataBase".equals(db.getDataBaseName()))
                {
                    listener.LocalDatabaseDies();
                }
                else if("remoteDataBase".equals(db.getDataBaseName()))
                {
                    listener.remoteDatabaseDies();
                }
            }
            else
            {
                if("localDataBase".equals(db.getDataBaseName()))
                {
                    listener.LocalDatabaseAlive();
                }
                else if("remoteDataBase".equals(db.getDataBaseName()))
                {
                    listener.remoteDataBaesAlive();
                }
            }
        }
    }
    
    public void remoteDbMaintain()
    {System.out.print("remote syste in maint\n");
        // check remote DB status see if we need to install remote DB
        if(!remoteDataBaseConnected_)
        {
            if(remoteDataBase_.testConnection())
            {
                remoteDataBaseConnected_ = (remoteDataBase_.selectDb(itsMysqlDb_) != null);
                if(!remoteDataBaseConnected_)
                {
                    if(createDbReq(itsMysqlDb_, Boolean.FALSE))
                    {
                        this.createTable(Boolean.FALSE);
                    }
                }
                else
                {
                    this.notifyDbStatusListeners(remoteDataBase_, remoteDataBaseConnected_);
                }
            }
        }
        else
        {
            try {
                remoteDataBaseConnected_ = remoteDataBase_.isDataBaseConectionAlive(5);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!remoteDataBaseConnected_)
            {
                this.notifyDbStatusListeners(remoteDataBase_, remoteDataBaseConnected_);
                remoteDataBase_.close();
            }
        }
        
        
    }
    
    public void localDbMaintain()
    {System.out.print("local syste in maint\n");
        // check remote DB status see if we need to install remote DB
        if(!localDataBaseConnected_)
        {
            if(localDataBase_.testConnection())
            {
                localDataBaseConnected_ = (localDataBase_.selectDb(itsMysqlDb_) != null);
                if(localDataBaseConnected_)
                {
                    this.notifyDbStatusListeners(localDataBase_, localDataBaseConnected_);
                }
            }
        }
        else
        {
            try {
                localDataBaseConnected_ = localDataBase_.isDataBaseConectionAlive(5);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!localDataBaseConnected_)
            {
                this.notifyDbStatusListeners(localDataBase_, remoteDataBaseConnected_);
                localDataBase_.close();
            }
        }
    }
    
    public Boolean createDbReq(Db_db db, Boolean localDb)
    {
        /*Db should already be created on both databases(remote&local)*/
        Boolean returnValue;
        if(localDb)
        {
            returnValue = (localDataBase_.createDb(db)!= null);
        }
        else
        {
            returnValue = (remoteDataBase_.createDb(db)!= null);
        }
        return returnValue;
    }
    
    public DbmReqHandler insertReq(Db_table table)
    {
        if(table != null)
        {
            return new DbmReqHandler(table,this, QUERY_TYPE.INSERT);
        }
        return null;
    }
    
    public DbmReqHandler selectReq(Db_table table)
    {
        if(table != null)
        {
            return new DbmReqHandler(table, this, QUERY_TYPE.SELECT);
        }
        return null;
    }
    
    public DbmReqHandler updateReq(Db_table table)
    {
        if(table != null)
        {
            return new DbmReqHandler(table, this, QUERY_TYPE.UPDATE);
        }
        return null;
    }
    
    public DbmReqHandler deleteReq(Db_table table)
    {
        if(table != null)
        {
            return new DbmReqHandler(table,this, QUERY_TYPE.DELETE);
        }
        return null;
    }
      
    private Boolean createTable(Boolean islocal)
    {
        Boolean result = Boolean.TRUE;
        Db_dataBase aimDataBase;
        if(Objects.equals(islocal, Boolean.FALSE))
        {
            aimDataBase = this.remoteDataBase_;
        }
        else
        {
            aimDataBase = this.localDataBase_;
        }
        
        Iterator<Entry<String, Db_table>> itr1 = this.itsL2Tables_.entrySet().iterator();
        while(itr1.hasNext())
        {
            Db_table tmpTable = itr1.next().getValue();
            if(!(aimDataBase.createTable(tmpTable)!=null))
            {
                result = Boolean.FALSE;
            }
        }
        
        Iterator<Entry<String, Db_table>> itr2 = this.itsMirrorTables_.entrySet().iterator();
        while(itr2.hasNext())
        {
            Db_table tmpTable = itr2.next().getValue();
            if(!(aimDataBase.createTable(tmpTable)!=null))
            {
                result = Boolean.FALSE;
            }
        }
        
        if(Objects.equals(islocal, Boolean.FALSE))
        {
            Iterator<Entry<String, Db_table>> itr3 = this.itsRemoteTables_.entrySet().iterator();
            while(itr3.hasNext())
            {
                Db_table tmpTable = itr3.next().getValue();
                if(!(aimDataBase.createTable(tmpTable)!=null))
                {
                    result = Boolean.FALSE;
                }
            }
        }
        return result;
    }
    
    public Db_dataBase getRemoteDataBase(){return this.remoteDataBase_;}
    public Db_dataBase getLocalDataBase(){return this.localDataBase_;}
    public Boolean remoteDatabaseOk(){return this.remoteDataBaseConnected_;}
    public Boolean localDatabaseOk(){return this.localDataBaseConnected_;}
    
    public Boolean shouldInstallRemote()
    {
        Db_db db = DataBaseFactory.newDb(db_type.MYSQL,this.itsCnf_.getDbName());
        return !this.remoteDataBase_.isDataBaseCreated(db);
    }
    
    public Boolean shouldInstallLocal()
    {
        Db_db db = DataBaseFactory.newDb(db_type.MYSQL,this.itsCnf_.getDbName());
        return !this.localDataBase_.isDataBaseCreated(db);
    }
    
    public ResultSet getRemoteSnycerTable(){return this.remoteDataBase_.getSyncTable();}
    public ResultSet getlocalSnycerTable(){return this.localDataBase_.getSyncTable();}
    public Db_table  findMirrorTable(String tblName){return this.itsMirrorTables_.get(tblName);}        
}
