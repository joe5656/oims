/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.Tasks;

import java.util.Timer;
import java.util.TimerTask;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_dataBase;

/**
 *
 * @author ezouyyi
 */
public class DataBaseKeepAlive  extends TimerTask {

    private Timer itsTimer_;
    private DataBaseManager itsdbm_;
    private Db_dataBase itsDb_;
    
    public DataBaseKeepAlive(Timer timer, DataBaseManager dbm, Db_dataBase type)
    {
        itsTimer_ = timer;
        itsdbm_ = dbm;
        itsDb_ = type;
    }
    
    public Timer  getTimer(){return itsTimer_;}
    
    @Override
    public void run() 
    {
        switch(itsDb_.getDataBaseName())
        {
            case "remoteDataBase":
            {
                itsdbm_.remoteDbMaintain();
                break;
            }
            case "localDataBase":
            {
                itsdbm_.localDbMaintain();
                break;
            }
            default:
            {}
        }
    }
    
}
