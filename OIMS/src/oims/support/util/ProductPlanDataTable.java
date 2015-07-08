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
    // Map<StoreName, Map<ProductName, Quantity>>
    private Map<String, Map<String,Integer>> productList_;
    private String serilizedInfo_;
    private Map<String, Integer>  productNameToId_;
    
    // format:
    // storeName1:productName1:quantity|storeName1:productName2:quantity|...|storeNameN:productNameN:quantity
    public ProductPlanDataTable(String serilizedInfo)
    {
        
    }
    
    public ProductPlanDataTable(Map<Integer, Map<Integer,Integer>> info)
    {
        
    }
    
    public ProductPlanDataTable()
    {
        
    }
    
    public Map<String, Map<String,Integer>> getProductList(){return productList_;}
    public Boolean addData(String storeName, String productName, Integer quantity)
    {
        Boolean result = false;
        if(this.productList_ != null)
        {
            if(this.productList_.containsKey(storeName))
            {
                Map<String, Integer> quantityMap = this.productList_.get(storeName);
                if(!quantityMap.containsKey(productName))
                {
                    quantityMap.put(productName, quantity);
                }
            }
            else
            {
                Map<String, Integer> quantityMap = Maps.newHashMap();
                quantityMap.put(productName, quantity);
                this.productList_.put(storeName, quantityMap);
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
            for(Entry<String,Map<String,Integer>> entry:this.productList_.entrySet())
            {
                String storeName = entry.getKey();
                for(Entry<String,Integer> quantityEntry:entry.getValue().entrySet())
                {
                    String productName = quantityEntry.getKey();
                    Integer quantity = quantityEntry.getValue();
                    this.serilizedInfo_ += (firstloop?"":"|")+storeName+":"+productName+":"+quantity;
                    firstloop = false;
                }
            }
        }
    }
    
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
                    String storeName = splitL2[0];
                    String productName = splitL2[1];
                    Integer quantity = Integer.parseInt(splitL2[2]);
                    if(this.productList_.containsKey(storeName))
                    {
                        Map<String, Integer> quantityMap = this.productList_.get(storeName);
                        if(!quantityMap.containsKey(productName))
                        {
                            quantityMap.put(productName, quantity);
                        }
                    }
                    else
                    {
                        Map<String, Integer> quantityMap = Maps.newHashMap();
                        this.productList_.put(storeName, quantityMap);
                        quantityMap.put(productName, quantity);
                    }
                }
            }
        }
    }
}
