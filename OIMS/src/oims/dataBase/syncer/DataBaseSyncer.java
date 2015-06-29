/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.syncer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_dataBase;
import oims.dataBase.Db_table;
import oims.dataBase.tables.SyncerTable;

/**
 *
 * @author ezouyyi
 */
public class DataBaseSyncer implements Runnable{
    private Boolean         isSyncerWorking_;
    private DataBaseManager itsDbm_;
    private List<Db_table>  itsChangedTables_;
    private SyncerTable     itsSyncerTable_;
    private List<Db_table>  itsL2CachingQuenen_;
    
    public DataBaseSyncer(DataBaseManager dbm, SyncerTable table)
    {
        itsDbm_ = dbm;
        isSyncerWorking_ = Boolean.TRUE;
        itsChangedTables_ = Lists.newArrayList();
        itsL2CachingQuenen_ = Lists.newArrayList();
        itsSyncerTable_ = table;
    }
    
    public void L2TableNeedToBeCached(Db_table table)
    {
        if(this.itsL2CachingQuenen_ != null)
        {
            this.itsL2CachingQuenen_.add(table);
        }
    }
    
    public void tableUpdated(Db_table table)
    {
        if(table.getTableType() == Db_table.Table_Type.TABLE_TYPE_MIRROR)
        {
            this.itsChangedTables_.add(table);
        }
    }
    
    public void start()
    {
        Thread syncerT = new Thread(this);
        syncerT.start();
    }
    
    public void stopSyncer()
    {
        isSyncerWorking_ = Boolean.FALSE;
    }
    
    public Boolean installEntry(Set<String> MirrorTables_)
    {
        Boolean result = Boolean.TRUE;
        for(String table:MirrorTables_)
        {
            result = result && this.itsSyncerTable_.newEntry(table);
        }
        return result;
    }
    
    private Set<String> checkSyncerTable()
    {
        Set<String> tableNeedDownload = Sets.newConcurrentHashSet();
        if(this.itsDbm_.isRemoteDbConnected() && this.itsDbm_.islocalDbConnected())
        {
            ResultSet localRs = this.itsDbm_.getlocalSnycerTable();
            ResultSet remoteRs = this.itsDbm_.getRemoteSnycerTable();
            tableNeedDownload = this.itsSyncerTable_.compareSyncerTable(localRs, remoteRs);
        }
        return tableNeedDownload;
    }
    
    @Override
    public void run() 
    {
        while(isSyncerWorking_)
        {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DataBaseSyncer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*update table*/
            for(Db_table table:this.itsChangedTables_)
            {
                this.itsSyncerTable_.tableUpdated(table);
            }
            
            /*prcess updated tables*/
            /*check syncerTable and process mirror tables*/
            Set<String> tableNeedDownload = checkSyncerTable();
            if(tableNeedDownload.size() > 0)
            {
                for(String str:tableNeedDownload)
                {
                    Db_table table = this.itsDbm_.findMirrorTable(str);
                    Db_dataBase lDb = this.itsDbm_.getLocalDataBase();
                    if(table != null)
                    {
                        this.itsDbm_.getRemoteDataBase().copyTable(lDb, table);
                    }
                }
                
                // Always sync syncerTable
                Db_table Stable = this.itsDbm_.findMirrorTable(SyncerTable.getDerivedTableName());
                if( Stable!= null)
                {
                    this.itsDbm_.getRemoteDataBase().copyTable(this.itsDbm_.getLocalDataBase(), Stable);
                }
            }
            
            /*process L2 tables*/
            if(this.itsL2CachingQuenen_.size() > 0)
            {
                for(Db_table table:this.itsL2CachingQuenen_)
                {
                    /*process one table once*/
                    if(table.uploadL2CachedData() == Boolean.TRUE)
                    {
                        this.itsL2CachingQuenen_.remove(table);
                    }
                    break;
                }
            }
        }
    }
    
    
}
