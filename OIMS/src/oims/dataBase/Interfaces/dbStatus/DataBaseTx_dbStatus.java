/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.Interfaces.dbStatus;

/**
 *
 * @author ezouyyi
 */
public interface DataBaseTx_dbStatus {
    public void remoteDatabaseDies();
    public void LocalDatabaseDies();
    public void remoteDataBaesAlive();
    public void LocalDatabaseAlive();
    public void SetRx(DataBaseRx_dbStatus rx);
}
