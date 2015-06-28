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
    
    public enum UNITS
    {
        gram,
        kilogram,
        lit,
        mlit,
    }
    
    public CommonUnit(String unit)
    {
        if(null != unit)
        switch (unit) {
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
            default:
                unit_ = UNITS.gram;
                break;
        }
        else
            unit_ = UNITS.gram;
    }
    
    CommonUnit(UNITS unit)
    {
        unit_ = unit;
    }
    
    public UNITS getUnit()
    {
        return unit_;
    }
    
    public Double getUnitChanageFactor(UNITS changeTo)
    {
        Double returnValue = 0.0;
        switch(changeTo)
        {
            case gram:
            {
                if(unit_ == UNITS.gram)
                {
                    returnValue = 1.0;
                }
                else if(unit_ == UNITS.kilogram)
                {
                    returnValue = 0.001;
                }
                break;
            }
            case kilogram:
            {
                if(unit_ == UNITS.gram)
                {
                    returnValue = 1000.0;
                }
                else if(unit_ == UNITS.kilogram)
                {
                    returnValue = 1.0;
                }
                break;
            }
            case lit:
            {
                if(unit_ == UNITS.mlit)
                {
                    returnValue = 1000.0;
                }
                else if(unit_ == UNITS.lit)
                {
                    returnValue = 1.0;
                }
                break;
            }
            case mlit:
            {
                if(unit_ == UNITS.mlit)
                {
                    returnValue = 1.0;
                }
                else if(unit_ == UNITS.lit)
                {
                    returnValue = 0.001;
                }
                break;
            }
        }
        return returnValue;
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
            default:
            {
                returnValue = "错误";
                break;                
            }
        }
        return returnValue;
    }
}
