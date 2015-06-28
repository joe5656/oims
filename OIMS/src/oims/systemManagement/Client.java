/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.systemManagement;

import oims.systemManagement.SystemManager.systemStatus;

/**
 *
 * @author freda
 */
public interface Client {
    
    public Boolean systemStatusChangeNotify(systemStatus status);
    public void setSystemManager(SystemManager sysManager);
    
}
