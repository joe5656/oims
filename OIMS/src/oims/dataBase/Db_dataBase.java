/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import oims.support.util.SqlResultInfo;

/**
 *
 * @author freda
 */
public interface Db_dataBase {

    public enum db_type
    {
        MYSQL
    }
    
    public String getDataBaseName();
    public Db_db createDb(Db_db dbToBeCreated);
    public Db_db createTable(Db_table tableToBeCreated);
    public void initDataBase(String user, String pw, String addr, String port);
    public Db_db selectDb(Db_db db);
    public SqlResultInfo insertRecord(Db_table.TableEntry entry, Db_table table);
    public Db_db currentDb();
    public SqlResultInfo select(List<String> selectList, Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml, Db_table table);
    public SqlResultInfo update(Db_table.TableEntry updateEntry, Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml, Db_table table);
    public SqlResultInfo  delete(Db_table.TableEntry entry_eq,Db_table.TableEntry entry_gr,Db_table.TableEntry entry_sml, Db_table table);
    public void close();
    public void setDbm(DataBaseManager dbm);
    public Boolean testConnection();
    public Boolean isDataBaseCreated(Db_db db);
    public Boolean isDataBaseConectionAlive(int timeout)  throws SQLException;
    public Boolean copyTable(Db_dataBase cpyTo, Db_table copyTable);
    public ResultSet getSyncTable();
    public db_type getType();
}
