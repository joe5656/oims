/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.reciptManagement;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import oims.UI.UiManager;
import oims.UI.pages.reciptPage.DetailReciptPickerTx;
import oims.UI.pages.reciptPage.ProductReciptPickerTx;
import oims.dataBase.DataBaseManager;
import oims.dataBase.tables.DetailReciptTable;
import oims.dataBase.tables.ProductReciptTable;
import oims.support.util.ProductPlanDataTable;
import oims.support.util.QuantitiedRawMaterial;
import oims.support.util.SqlDataTable;
import oims.support.util.SqlResultInfo;
import oims.systemManagement.SystemManager;

/**
 *
 * @author ezouyyi
 */
public class ReciptManager  implements oims.systemManagement.Client{
    private SystemManager    itsSysManager_;
    private final ProductReciptTable itsProductReciptTable_;
    private final DetailReciptTable  itsDetailReciptTable_;
    private DataBaseManager  itsdbm_;
    
    public ReciptManager(DataBaseManager dbm)
    {
        itsProductReciptTable_ = new ProductReciptTable(dbm);
        itsDetailReciptTable_  = new DetailReciptTable(dbm);
        itsdbm_ = dbm;
    }
        
    public SqlResultInfo newProductRecipt(String pName, Integer number, Integer singleWeightInGram, 
            String mainName, String mainFactor, String topName, String topFactor, 
            String fillingName, String fillFactor, Integer workinghours, 
            Boolean mainReciptByCK,Boolean toppingByCK, Boolean fillingByCk )
    {
        if(itsProductReciptTable_.reciptForProductExsited(pName))
        {
            return this.itsProductReciptTable_.update(number, pName, number, mainName, 
                    mainFactor, topName, topFactor, fillingName, fillFactor, workinghours, 
                    mainReciptByCK, toppingByCK, fillingByCk, singleWeightInGram);
        }
        else
        {
            return this.itsProductReciptTable_.newEntry(pName, number, mainName, mainFactor,
                topName, topFactor, fillingName, fillFactor, workinghours,
                singleWeightInGram, mainReciptByCK, toppingByCK, fillingByCk);
        }
  }
    
    public SqlResultInfo newDetailRecipt(String reciptName, List<QuantitiedRawMaterial> rms)
    {
        String recipt = DetailRecipt.serialize(rms);
        if(this.itsDetailReciptTable_.reciptExsited(reciptName))
        {
            return this.itsDetailReciptTable_.update(reciptName, recipt);
        }
        else
        {
            return this.itsDetailReciptTable_.newEntry(reciptName, recipt);
        }
    }
    
    public SqlDataTable queryDetailReciptAll()
    {
        return new SqlDataTable(this.itsDetailReciptTable_.query(null, null).getResultSet(),
                this.itsDetailReciptTable_.getName());
    }
    
    public SqlDataTable queryProductReciptAll()
    {
        return new SqlDataTable(this.itsProductReciptTable_.query(null, null).getResultSet(),
                this.itsDetailReciptTable_.getName());
    }
    
    public SqlDataTable queryProductRecipt(String reciptId, String productName)
    {
        return new SqlDataTable(this.itsProductReciptTable_.query(Integer.parseInt(reciptId), productName).getResultSet(),
                this.itsDetailReciptTable_.getName());
    }
    
    /*
    calculate the detailRecipt list 
    input productMap <productName, neededNumber>
    output reciptMap <reciptName, factor>
    */
    public Map<String, Double> calDetailReciptList(ProductPlanDataTable productMap)
    {
        Map<String, Double> result = Maps.newHashMap();
        Map<String, ProductRecipt> productRectip = getAll();
        if(!productRectip.isEmpty() && productMap.productNum()>0 && productMap.initItr())
        {
            do
            {
                String key = productMap.getProductName();
                if(productRectip.containsKey(key))
                {
                    ProductRecipt temp = productRectip.get(key);
                    Integer       needNum = Integer.parseInt(productMap.getProductQuantity());
                    Integer       standNum = temp.getStandNum();
                    if(standNum == 0){continue;}
                    Double        factor   = new Double(needNum/standNum);
                    
                    String mr = temp.getMainReciptName();
                    String tr = temp.getTopReciptName();
                    String fr = temp.getFillReciptName();
                    Double mf = temp.getMainFactor();
                    Double tf = temp.getTopFactor();
                    Double ff = temp.getFillFactor();
                    
                    if(mr != "NONE")
                    {
                        if(result.containsKey(mr))
                        {
                            result.replace(mr, result.get(mr) + Math.floor(mf*factor*10)/10);
                        }
                        else
                        {
                            result.put(mr, Math.floor(mf*factor*10)/10);
                        }
                    }
                    if(tr != "NONE")
                    {
                        if(result.containsKey(tr))
                        {
                            result.replace(tr, result.get(tr) + Math.floor(tf*factor*10)/10);
                        }
                        else
                        {
                            result.put(tr, Math.floor(tf*factor*10)/10);
                        }
                    }
                    if(fr != "NONE")
                    {
                        if(result.containsKey(fr))
                        {
                            result.replace(fr, result.get(fr) + Math.floor(ff*factor*10)/10);
                        }
                        else
                        {
                            result.put(fr, Math.floor(ff*factor*10)/10);
                        }
                    }
                }
            }while(productMap.next());
        }
        
        return result;
    }
    
    private Map<String, ProductRecipt> getAll()
    {
        return this.itsProductReciptTable_.queryAll();
    }
    
    @Override
    public Boolean systemStatusChangeNotify(SystemManager.systemStatus status)
    {
        switch(status)
        {
            case SYS_INIT:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
            case SYS_CONFIG:
            {
                
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
            case SYS_REGISTER:
            {
                this.itsdbm_.registerTable(itsProductReciptTable_);
                this.itsdbm_.registerTable(itsDetailReciptTable_);
                break;
            }
            case SYS_START:
            {
                
                break;
            }
            default:
            {
                itsSysManager_.statusChangeCompleted(Boolean.TRUE, "rmm");
                break;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void setSystemManager(SystemManager sysManager){itsSysManager_= sysManager;}
    
    public void needDetailReciptPicker(DetailReciptPickerTx tx, Integer id)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        SqlDataTable table = this.queryDetailReciptAll();
        this.itsDetailReciptTable_.translateColumnName(table.getColumnNames());
        tempUiM.showDetailReciptPicker(table, tx, id);
    }
    
    public void needProductReciptPicker(ProductReciptPickerTx tx, Integer id)
    {
        UiManager tempUiM = (UiManager)itsSysManager_.getClient(SystemManager.clientType.UI_MANAGER);
        SqlDataTable table = this.queryProductReciptAll();
        this.itsProductReciptTable_.translateColumnName(table.getColumnNames());
        tempUiM.showrProductReciptPicker(table, tx, id);
    }
}
