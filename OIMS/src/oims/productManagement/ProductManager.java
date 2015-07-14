/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.productManagement;

import oims.dataBase.tables.ProductTable;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.EmployeeTable;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class ProductManager implements oims.systemManagement.Client{
    Map<Integer, ProductTable> loadedProduct_;
    private SystemManager itsSysManager_;
    private ProductTable  itsProductTable_;
    
    public ProductManager(DataBaseManager dbm)
    {
        itsProductTable_ = new ProductTable(dbm);
    }
    
    public SqlResultInfo createNewProduct(String produtName, Double price, Double vipPrice, String cat,
                            String picUrl, String nameAbbr)
    {
        return this.itsProductTable_.newEntry(produtName, price, vipPrice, cat, picUrl, nameAbbr, 0.0);
    }
    
    public SqlResultInfo toggleProduct(Integer productId, Boolean isActive)
    {
        return this.itsProductTable_.update(productId, null, null, isActive, null, null, null, null, null);
    }
    
    public SqlDataTable queryProduct(Integer productId, Boolean isActive)
    {
        SqlResultInfo result = this.itsProductTable_.query(productId, isActive, null);
        SqlDataTable returnValue;
        if(result.isSucceed())
        {
            returnValue = new SqlDataTable(result.getResultSet(),this.itsProductTable_.getName());
        }
        else
        {
            returnValue = new SqlDataTable(null,this.itsProductTable_.getName());
        }
        return returnValue;
    }
    
    public Boolean forceRefreshLoadList()
    {
        return false;
    }
    
    public Boolean isAllProductLoaded()
    {
        return false;
    }

    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status) {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
            case SYS_REGISTER:
            {
                itsProductTable_.registerToDbm();
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "epm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager) {itsSysManager_ = sysManager;}
}
