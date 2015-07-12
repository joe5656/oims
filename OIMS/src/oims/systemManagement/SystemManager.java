/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.systemManagement;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import oims.dataBase.DataBaseManager;
import oims.employeeManager.Employee;

/**
 *
 * @author freda
 */
public class SystemManager {
    private final Map<clientType, Client> itsClients_;
    private systemStatus sysStatus_;
    private Integer      numberOfCompletedModules_;
    private Employee     curEmployee_;
    
    public enum clientType
    {
        UI_MANAGER,
        WAREHOUSE_MANAGER,
        DATABASE_MANAGER,
        TICKET_MANAGER,
        EMPLOYEE_MANAGER,
        RAWMATERIAL_MANAGER,
        STORE_MANAGER,
        PRODUCT_MANAGER
    }
    public enum systemStatus
    {
        INSTALL_DB,
        INSTALL,
        SYS_INIT,
        SYS_REGISTER,
        SYS_CONFIG,
        SYS_START,
        SYS_ACTIVE,
        SYS_LOCKED,
        SYS_SHUTDOWN
    }
    
    public Client getClient(clientType type)
    {
        Client client = null;
        if(this.itsClients_.containsKey(type))
        {
            client = itsClients_.get(type);
        }
        return client;
    }
    
    public SystemManager()
    {
        itsClients_ = Maps.newHashMap();
        numberOfCompletedModules_ = 0;
    }
    
    public Client registerClient(clientType type, Client client)
    {
        client.setSystemManager(this);
        return itsClients_.put(type, client);
    }
    
    public void statusChangeCompleted(Boolean phaseSucess, String moduleName)
    {/*
        if(phaseSucess)
        {
            numberOfCompletedModules_ ++;
            if(numberOfCompletedModules_ == itsClients_.size())
            {
                if(sysStatus_  != systemStatus.SYS_ACTIVE)
                {
                    goToNextStatus();
                }
            }
        }*/
    }

    private void goToNextStatus()
    {
        if(sysStatus_ == systemStatus.SYS_INIT)
        {
            sysStatus_ = systemStatus.SYS_REGISTER;
        }
        else if(sysStatus_ == systemStatus.SYS_REGISTER)
        {
            sysStatus_ = systemStatus.SYS_CONFIG;
        }
        else if(sysStatus_ == systemStatus.SYS_CONFIG)
        {
            if(ifShouldInstall())
            {
                sysStatus_ = systemStatus.INSTALL_DB;
            }
            else
            {
                sysStatus_ = systemStatus.SYS_START;
            }
        }
        else if(sysStatus_ == systemStatus.INSTALL_DB)
        {
            sysStatus_ = systemStatus.INSTALL;
        }
        else if(sysStatus_ == systemStatus.INSTALL)
        {
            sysStatus_ = systemStatus.SYS_START;
        }
        else if(sysStatus_ == systemStatus.SYS_START)
        {
            sysStatus_ = systemStatus.SYS_ACTIVE;
        }
        
        numberOfCompletedModules_ = 0;
        notifyAllClients();
    }
    
    private void notifyAllClients()
    {
        Set<Map.Entry<clientType, Client>> entries;
        entries = this.itsClients_.entrySet();
        Iterator<Entry<clientType, Client>> itr = entries.iterator();
        Boolean resultValue = Boolean.TRUE;
        while(itr.hasNext())
        {
            resultValue = resultValue && itr.next().getValue().systemStatusChangeNotify(sysStatus_);
        }
        if(sysStatus_ != systemStatus.SYS_ACTIVE && resultValue == Boolean.TRUE)
        {
            this.goToNextStatus();
        }
        else if(resultValue == Boolean.FALSE)
        {
            System.exit(0);
        }
    }
    
    private Boolean ifShouldInstall()
    {
        DataBaseManager dbm = (DataBaseManager)this.itsClients_.get(clientType.DATABASE_MANAGER);
        return dbm.shouldInstallLocal();
    }
    
    public void run()
    {
        sysStatus_ = systemStatus.SYS_INIT;
        notifyAllClients();
        
    }
}
