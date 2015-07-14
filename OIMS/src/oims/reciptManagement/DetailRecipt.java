/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.reciptManagement;

import com.google.common.collect.Lists;
import java.util.List;
import oims.support.util.QuantitiedRawMaterial;

/**
 *
 * @author ezouyyi
 */
public class DetailRecipt {
    static public String serialize(List<QuantitiedRawMaterial> rms)
    {
        String result = "";
        Boolean firstLoop = true;
        Integer loopCnt = 0;
        for(QuantitiedRawMaterial entry:rms)
        {
            loopCnt++;
            if(firstLoop || loopCnt == rms.size())
            {
                firstLoop = false;
            }
            else
            {
                result += "|";
            }
            //MaterialId1:MaterialName1:quantity1:unit1
            result += entry.getRmId().trim()+":"+entry.getRmName().trim()+":"
                    +entry.getRmQuantity().trim()+":"+entry.getRmUnitName().trim();
        }
        return result;
    }
    
    static public List<QuantitiedRawMaterial> unSerialize(String recipt)
    {
        List<QuantitiedRawMaterial> returnList = Lists.newArrayList();
        String[] rms = recipt.split("|");
        for(String entry:rms)
        {
            String[] detailInfo = entry.split(":");
            QuantitiedRawMaterial tmpRm = new QuantitiedRawMaterial(detailInfo[0],
                detailInfo[1],detailInfo[2],detailInfo[3]);
            returnList.add(tmpRm);
        }
        return returnList;
    }
}
