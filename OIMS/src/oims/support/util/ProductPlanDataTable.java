/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author ezouyyi
 */
public class ProductPlanDataTable {
    // Map<StoreId, Map<ProductId, Quantity>>
    private Map<Integer, Map<Integer,Integer>> productList_;
    private String serilizedInfo_;
    
    // format:
    // storeId1:productId1:quantity|storeId1:productId2:quantity|...|storeIdN:productIdN:quantity
    public ProductPlanDataTable(String serilizedInfo)
    {
        
    }
    
    public ProductPlanDataTable(Map<Integer, Map<Integer,Integer>> info)
    {
        
    }
    
    public ProductPlanDataTable()
    {
        
    }
    
    public Boolean addData(Integer storeId, Integer productId, Integer quantity)
    {
        Boolean result = false;
        if(this.productList_ != null)
        {
            if(this.productList_.containsKey(storeId))
            {
                Map<Integer, Integer> quantityMap = this.productList_.get(storeId);
                if(!quantityMap.containsKey(productId))
                {
                    quantityMap.put(productId, quantity);
                }
            }
            else
            {
                Map<Integer, Integer> quantityMap = Maps.newHashMap();
                quantityMap.put(productId, quantity);
                this.productList_.put(storeId, quantityMap);
            }
        }
        return result;
    }
    
    public String getSerilizedInfo()
    {
        this.serializeInfo();
        return this.serilizedInfo_;
    }
    
    private void serializeInfo()
    {
        this.serilizedInfo_ = "";
        if(this.productList_ != null)
        {
            Boolean firstloop = true;
            for(Entry<Integer,Map<Integer,Integer>> entry:this.productList_.entrySet())
            {
                Integer storeId = entry.getKey();
                for(Entry<Integer,Integer> quantityEntry:entry.getValue().entrySet())
                {
                    Integer productId = quantityEntry.getKey();
                    Integer quantity = quantityEntry.getValue();
                    this.serilizedInfo_ += (firstloop?"":"|")+storeId+":"+productId+":"+quantity;
                    firstloop = false;
                }
            }
        }
    };
    
    private void unserializeInfo(String serilizedInfo)
    {
        if(serilizedInfo!=null)
        {
            this.productList_.clear();
            String[] splitL1 =  serilizedInfo.split("\\|");
            for(String str:splitL1)
            {
                String[] splitL2 = str.split(":");
                if(splitL2.length == 3)
                {
                    Integer storeId = Integer.parseInt(splitL2[0]);
                    Integer productId = Integer.parseInt(splitL2[1]);
                    Integer quantity = Integer.parseInt(splitL2[2]);
                    if(this.productList_.containsKey(storeId))
                    {
                        Map<Integer, Integer> quantityMap = this.productList_.get(storeId);
                        if(!quantityMap.containsKey(productId))
                        {
                            quantityMap.put(productId, quantity);
                        }
                    }
                    else
                    {
                        Map<Integer, Integer> quantityMap = Maps.newHashMap();
                        this.productList_.put(storeId, quantityMap);
                        quantityMap.put(productId, quantity);
                    }
                }
            }
        }
    };
}
