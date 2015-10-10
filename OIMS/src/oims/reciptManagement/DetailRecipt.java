/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.reciptManagement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import oims.support.util.QuantitiedRawMaterial;

/**
 *
 * @author ezouyyi
 */
public class DetailRecipt {
    private String detailReciptName_;
    private String serializedRecipt_;
    private Map<String, QuantitiedRawMaterial> recipt_;
    private Iterator<Entry<String, QuantitiedRawMaterial>> Itr_;
    
    public DetailRecipt()
    {
        recipt_ = Maps.newHashMap();
        this.detailReciptName_ = "NA";
        this.serializedRecipt_ = "NA";
    }
    
    public DetailRecipt(String reciptName, String serilizedString)
    {
        this.detailReciptName_ = reciptName;
        this.serializedRecipt_ = serilizedString;
        recipt_ = DetailRecipt.unSerialize(serilizedString);
    }
    
    public void initItr()
    {
        Itr_ = this.recipt_.entrySet().iterator();
    }
    
    public Boolean hasNextMaterial(){return this.Itr_.hasNext();}
    public Entry<String, QuantitiedRawMaterial> nextMaterial(){return this.Itr_.next();}
    
    static public String serialize(List<QuantitiedRawMaterial> rms)
    {
        String result = "";
        Boolean firstLoop = true;
        Integer loopCnt = 0;
        for(QuantitiedRawMaterial entry:rms)
        {
            loopCnt++;
            if(firstLoop)
            {
                firstLoop = false;
            }
            else
            {
                result += "|";
            }
            //MaterialName1:quantity1:unit1
            result += entry.getRmName().trim()+":"
                    +entry.getRmQuantity().trim()+":"+entry.getRmUnitName().trim();
        }
        return result;
    }
    
    static public Map<String, QuantitiedRawMaterial> unSerialize(String recipt)
    {
        Map<String, QuantitiedRawMaterial> returnMap = Maps.newHashMap();
        String[] rms = recipt.split("|");
        for(String entry:rms)
        {
            String[] detailInfo = entry.split(":");
            QuantitiedRawMaterial tmpRm = new QuantitiedRawMaterial(
                detailInfo[0],detailInfo[1],detailInfo[2]);
            returnMap.put(detailInfo[2],tmpRm);
        }
        return returnMap;
    }
}
