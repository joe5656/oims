/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 *
 * @author ezouyyi
 */
public class ProductPlanDataTable {
    // Map<ProductName, Quantity>
    private Map<String, String> productList_;
    private String serilizedInfo_;
    private Map<String, Integer>  productNameToId_;
    private Iterator<Entry<String, String>> tmepItr_;
    private Entry<String, String> tmpHolder_;
    // format:
    // productName1:quantity1|productName2:quantity2|...|productNameN:quantityN
    public ProductPlanDataTable(String serilizedInfo)
    {
        serilizedInfo_ = serilizedInfo;
        productNameToId_ = Maps.newHashMap();
        productList_ = Maps.newHashMap();
        unserializeInfo(serilizedInfo_);
    }
    
    public ProductPlanDataTable(Map<Integer, Map<Integer,Integer>> info)
    {
        productNameToId_ = Maps.newHashMap();
        productList_ = Maps.newHashMap();
    }
    
    public ProductPlanDataTable()
    {
        productNameToId_ = Maps.newHashMap();
        productList_ = Maps.newHashMap();
    }
    
    public Map<String, String> getProductList(){return productList_;}
    public Boolean addData(String productName, String productId, String quantity)
    {
        Boolean result = false;
        if(this.productList_ != null)
        {
            if(!this.productList_.containsKey(productName))
            {
                this.productList_.put(productName, quantity);
            }
            else
            {
                this.productList_.replace(productName, quantity);
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
            for(Entry<String,String> entry:this.productList_.entrySet())
            {
                String productName = entry.getKey();
                String quantity = entry.getValue();
                this.serilizedInfo_ += (firstloop?"":"|")+productName+":"+quantity;
                firstloop = false;
            }
        }
    }
    
    public Boolean initItr()
    {
        this.tmepItr_ = this.productList_.entrySet().iterator();
        if(tmepItr_ != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean next()
    {
        Boolean result = false;
        if(tmepItr_.hasNext())
        {
            tmpHolder_ = tmepItr_.next();
            result = true;
        }
        return result;
    }
    
    public String getProductName()
    {
        String result = "NA";
        if(tmpHolder_ != null)
        {
            result = tmpHolder_.getKey();
        }
        return result;
    }
    
    public String getProductQuantity()
    {
        String result = "NA";
        if(tmpHolder_ != null)
        {
            result = tmpHolder_.getValue();
        }
        return result;
    }
    
    public Integer productNum()
    {
        return this.productList_.size();
    }
    
    private void unserializeInfo(String serilizedInfo)
    {
        if(serilizedInfo!=null)
        {
            this.productList_.clear();
            String[] splitL1 =  serilizedInfo.split("|");
            for(String str:splitL1)
            {
                String[] splitL2 = str.split(":");
                if(splitL2.length == 2)
                {
                    String productName = splitL2[0];
                    String quantity = splitL2[1];
                    if(this.productList_.containsKey(productName))
                    {
                        this.productList_.replace(productName, quantity);
                    }
                    else
                    {
                        this.productList_.put(productName, quantity);
                    }
                }
            }
        }
    }
}
