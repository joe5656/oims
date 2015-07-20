/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

/**
 *
 * @author ezouyyi
 */
public class CommonUnit {
    
    private final UNITS unit_;
    private final String unitTransferAid_;
    
    public enum UNITS
    {
        none,
        gram,
        kilogram,
        lit,
        mlit,
        bottle,
        unit,
        bag
    }
    
    static public String[] getUnitListStringChn()
    {
        String[] result = { "克","千克", "袋装", "瓶装", "升", "毫升", "个"};
        return result;
    }
    
    static public String[] getStanderizedUnitChn()
    {
        String[] result = {"千克", "克", "升", "毫升", "个"};
        return result;
    }
    
    static public String[] getUnstanderizedUnitChn()
    {
        String[] result = { "袋装", "瓶装"};
        return result;
    }
    
    public CommonUnit(String unit)
    {
        if(null != unit)
        {
            Integer index = 0;
            
            String transferredunit;
            if(unit.contains("袋装")){transferredunit = "袋装";index = unit.indexOf("袋装");}
            else if(unit.contains("瓶装")){transferredunit = "瓶装";index = unit.indexOf("瓶装");}
            else if(unit.contains("bottle")){transferredunit = "bottle";index = unit.indexOf("bottle");}
            else if(unit.contains("bag")){transferredunit = "bag";index = unit.indexOf("bag");}
            else{transferredunit = unit;}
            
            if(index > 0)
            {
                unitTransferAid_ = unit.substring(0, index);
            }
            else
            {
                unitTransferAid_ = null;
            }
            
            switch (transferredunit) {
            case "gram":
            case "克":
                unit_ = UNITS.gram;
                break;
            case "kilogram":
            case "千克":
                unit_ = UNITS.kilogram;
                break;
            case "lit":
            case "升":
                unit_ = UNITS.lit;
                break;
            case "mlit":
            case "毫升":
                unit_ = UNITS.mlit;
                break;
            case "bag":
            case "袋装":
                unit_ = UNITS.bag;
                break;
            case "bottle":
            case "瓶装":
                unit_ = UNITS.bottle;
                break;
            case "unit":
            case "个":
                unit_ = UNITS.unit;
                break;
            default:
                unit_ = UNITS.none;
                break;
            }
        }
        else
        {
            unitTransferAid_ = null;
            unit_ = UNITS.gram;
        }
    }
    
    public UNITS getUnit()
    {
        return unit_;
    }
    
    public Double getUnitChanageFactor(UNITS changeTo)
    {
        Double returnValue = -1.0;
        Double baseNumber = 1.0;
        UNITS  toBeChanged = unit_;

        if(unit_ == UNITS.bag || unit_ == UNITS.bottle)
        {
            if(this.unitTransferAid_ != null)
            {
                Integer index = -1;
                if(unitTransferAid_.contains("千克") || unitTransferAid_.contains("kilogram"))
                {
                    index = !unitTransferAid_.contains("千克")?
                                unitTransferAid_.indexOf("kilogram"):
                                unitTransferAid_.indexOf("千克");
                    if(index > 0)
                    {
                        toBeChanged = UNITS.kilogram;
                    }
                }
                else if(unitTransferAid_.contains("克") || unitTransferAid_.contains("gram"))
                {
                    index = !unitTransferAid_.contains("克")?
                                unitTransferAid_.indexOf("gram"):
                                unitTransferAid_.indexOf("克");
                    if(index > 0)
                    {
                        toBeChanged = UNITS.gram;
                    }
                }
                else if(unitTransferAid_.contains("毫升") || unitTransferAid_.contains("mlit"))
                {
                    index = !unitTransferAid_.contains("毫升")?
                                unitTransferAid_.indexOf("mlit"):
                                unitTransferAid_.indexOf("毫升");
                    if(index > 0)
                    {
                        toBeChanged = UNITS.mlit;
                    }
                }
                else if(unitTransferAid_.contains("升") || unitTransferAid_.contains("lit"))
                {
                    index = !unitTransferAid_.contains("升")?
                                unitTransferAid_.indexOf("lit"):
                                unitTransferAid_.indexOf("升");
                    if(index > 0)
                    {
                        toBeChanged = UNITS.lit;
                    }
                }

                if(index > 0)
                {
                    baseNumber = Double.parseDouble(unitTransferAid_.substring(0,index));
                }
            }
        }
        switch(changeTo)
        {
            case gram:
            {
                if(toBeChanged == UNITS.gram)
                {
                    returnValue = 1.0;
                }
                else if(toBeChanged == UNITS.kilogram)
                {
                    returnValue = 1000.0;
                }
                break;
            }
            case kilogram:
            {
                if(toBeChanged == UNITS.gram)
                {
                    returnValue = 0.001;
                }
                else if(toBeChanged == UNITS.kilogram)
                {
                    returnValue = 1.0;
                }
                break;
            }
            case lit:
            {
                if(toBeChanged == UNITS.mlit)
                {
                    returnValue = 0.001;
                }
                else if(toBeChanged == UNITS.lit)
                {
                    returnValue = 1.0;
                }
                break;
            }
            case mlit:
            {
                if(toBeChanged == UNITS.mlit)
                {
                    returnValue = 1.0;
                }
                else if(toBeChanged == UNITS.lit)
                {
                    returnValue = 1000.0;
                }
                break;
            }
        }
        return (baseNumber>0)?returnValue*baseNumber:returnValue;
    }
    
    public UNITS standeriseUnit()
    {
        if(unit_ == UNITS.bag || unit_ == UNITS.bottle)
        {
            if(this.unitTransferAid_ != null)
            {
                if(unitTransferAid_.contains("千克") 
                        || unitTransferAid_.contains("kilogram")
                        || unitTransferAid_.contains("克")
                        || unitTransferAid_.contains("gram"))
                {
                    return UNITS.gram;
                }
                else if(unitTransferAid_.contains("升") 
                        || unitTransferAid_.contains("mlit")
                        || unitTransferAid_.contains("毫升")
                        || unitTransferAid_.contains("lit"))
                {
                    return UNITS.mlit;
                }
            }
        }
        return unit_;
    }
    
    public String getUnitName()
    {
        String returnValue;
        switch(unit_)
        {
            case gram:
            {
                returnValue = "克";
                break;
            }
            case kilogram:
            {
                returnValue = "千克";
                break;
            }
            case lit:
            {
                returnValue = "升";
                break;
            }
            case mlit:
            {
                returnValue = "毫升";
                break;
            }
            case bottle:
            {
                returnValue = "瓶装";
                break;
            }
            case bag:
            {
                returnValue = "袋装";
                break;
            }
            case unit:
            {
                returnValue = "个";
                break;
            }
            default:
            {
                returnValue = "错误";
                break;                
            }
        }
        return this.unitTransferAid_==null?returnValue:unitTransferAid_+returnValue;
    }
}
