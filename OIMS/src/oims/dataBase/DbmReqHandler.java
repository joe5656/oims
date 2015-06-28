/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import static oims.dataBase.Db_table.Table_Type.TABLE_TYPE_REMOTE;

/**
 *
 * @author ezouyyi
 */
public class DbmReqHandler{
    DataBaseManager itsDbm_;
    Db_table.TableEntry  entry_;
    Db_table.TableEntry  equalEntry_;
    Db_table.TableEntry  greatThanEntry_;
    Db_table.TableEntry  smallerThanEntry_;
    Db_table    itsTable_;
    DataBaseManager.QUERY_TYPE  reqType_;

    public DbmReqHandler(Db_table table, DataBaseManager dbm, DataBaseManager.QUERY_TYPE type)
    {
        itsDbm_ = dbm;
        itsTable_ = table;
        reqType_ = type;
    }

    public void fillInData(Db_table.TableEntry  entry)
    {
        entry_ = entry;
    }

    public void fillInEqData(Db_table.TableEntry  entry)
    {
        equalEntry_ = entry;
    }

    public void fillInGrData(Db_table.TableEntry  entry)
    {
        greatThanEntry_ = entry;
    }

    public void fillInSmlData(Db_table.TableEntry  entry)
    {
        smallerThanEntry_ = entry;
    }

    public Object execute()
    {
        Object result;
        switch(reqType_)
        {
            case INSERT:
            {
                result = false;
                // type == insert, entry works as data to be inserted
                switch(itsTable_.getTableType())
                {
                    case TABLE_TYPE_REMOTE:
                    case TABLE_TYPE_MIRROR:
                    {
                        if(itsDbm_.remoteDatabaseOk())
                        {
                            result = itsDbm_.getRemoteDataBase().insertRecord(entry_, itsTable_);
                            if((Boolean)result == true && 
                                    itsTable_.getTableType() == Db_table.Table_Type.TABLE_TYPE_MIRROR)
                            {
                                this.itsDbm_.inforSycnerTableUpdated(itsTable_);
                            }
                        }
                        break;
                    }
                    case TABLE_TYPE_2_LEVEL:
                    {
                        Boolean insertLocal = Boolean.TRUE;
                        if(itsDbm_.remoteDatabaseOk())
                        {
                            if(Objects.equals(itsDbm_.getRemoteDataBase().insertRecord(entry_, itsTable_), Boolean.TRUE))
                            {
                                result = Boolean.TRUE;
                                insertLocal = Boolean.FALSE;
                            }
                        }
                        if(insertLocal == Boolean.TRUE)
                        {
                            result = itsDbm_.getLocalDataBase().insertRecord(entry_, itsTable_);
                        }
                        break;
                    }
                }
                break;
            }
            case SELECT:
            {
                // type == SELECT, entry works as select list columns with 
                // none-null value in entry map is selected if all columns is
                // null it stands for select count(*)
                // where clause
                // if the values in equalEntry_ is not null then the entry will
                // be used as an equal clasus
                // for instance: equalEntry_<"key", "value(not null)">
                // then in sql "where key = value" will be added
                result = null;
                List<String> selectList = Lists.newArrayList();
                if(entry_ != null)
                {
                    entry_.generateSelectList(selectList);
                }
                
                switch(itsTable_.getTableType())
                {
                    case TABLE_TYPE_REMOTE:
                    case TABLE_TYPE_2_LEVEL:
                    {
                        if(itsDbm_.remoteDatabaseOk())
                        {
                            return itsDbm_.getRemoteDataBase().select(selectList, equalEntry_, greatThanEntry_, smallerThanEntry_, itsTable_);
                        }
                        break;
                    }
                    case TABLE_TYPE_MIRROR:
                    {
                        if(itsDbm_.remoteDatabaseOk())
                        {
                            result = itsDbm_.getRemoteDataBase().select(selectList, equalEntry_, greatThanEntry_, smallerThanEntry_, itsTable_);
                        }
                        
                        if(result == null)
                        {
                            result = itsDbm_.getLocalDataBase().insertRecord(entry_, itsTable_);
                        }
                        break;
                    }
                }
                break;
            }
            /*only update remote tables, L2 table can not be updated, mirror tables
            should not be updated initialtively it should be synced*/
            case UPDATE:
            {
                result = false;
                if(itsTable_.getTableType() == Db_table.Table_Type.TABLE_TYPE_2_LEVEL)
                {
                    // update for L2 table is forbiden
                }
                else
                {
                    result = itsDbm_.getRemoteDataBase().update(entry_, 
                            equalEntry_, greatThanEntry_, smallerThanEntry_, itsTable_);
                    if((Boolean)result == true && 
                            itsTable_.getTableType() == Db_table.Table_Type.TABLE_TYPE_MIRROR)
                    {
                        this.itsDbm_.inforSycnerTableUpdated(itsTable_);
                    }
                }
                break;
            }
            case DELETE:
            {
                result = itsDbm_.getRemoteDataBase().delete(equalEntry_, greatThanEntry_, 
                        smallerThanEntry_, itsTable_);
                if((Boolean)result == true && 
                        itsTable_.getTableType() == Db_table.Table_Type.TABLE_TYPE_MIRROR)
                {
                    this.itsDbm_.inforSycnerTableUpdated(itsTable_);
                }
                break;
            }
            default:
            {
                result = null;
            }
        }
        return result;
    }
}
