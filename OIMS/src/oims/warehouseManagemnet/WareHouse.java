/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.warehouseManagemnet;

import oims.rawMaterialManagement.RawMaterial;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oims.dataBase.tables.StackTable;
import oims.dataBase.tables.WareHouseTable;
import oims.support.util.UnitQuantity;

/**
 *
 * @author ezouyyi
 */
public class WareHouse {
    Integer id_;
    String  name_;
    Integer itsKeeperId_;
    String  addr_;
    String  contact_;
    WareHouseTable itsWareHouseTable_;
    StackTable     itsStackTable_;
    Boolean syncd_;
    
    private WareHouse(){};
    private void defaultWareHouse()
    {
        id_ = -1;
        name_ = null;
        syncd_ = Boolean.FALSE;
    };
    
    public WareHouse(Integer id, WareHouseTable WareHouseTable, StackTable  stack)
    {
        defaultWareHouse();
        id_ = id;
        itsWareHouseTable_ = WareHouseTable;
        itsStackTable_ = stack;
    };
    
    public WareHouse(String name, WareHouseTable WareHouseTable, StackTable  stack)
    {
        defaultWareHouse();
        name_ = name;
        itsWareHouseTable_ = WareHouseTable;
        itsStackTable_ = stack;
    };
    

    public void setId(Integer id){id_ = id;}
    public void setName(String name){name_ = name;}
    public void setKeeper(Integer id){this.itsKeeperId_ = id;}
    public void setAddr(String addr){addr_ = addr;}
    public void setContact(String con){contact_ = con;}   
    public void setSyncd(){syncd_ = Boolean.TRUE;}
    
    public Integer getId(){return id_;}
    public String  getName(){return name_;}
    public Integer getKeeperId(){return itsKeeperId_;}
    public String  getAddr(){return addr_;}
    public String  getContact(){return contact_;}
    public Boolean isSyncd(){return syncd_;}      
    
    public void serialize()
    {
        try {
            itsWareHouseTable_.serializeWareHouseInstance(this);
        } catch (SQLException ex) {
            Logger.getLogger(WareHouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean checkIn(RawMaterial rm, UnitQuantity uq)
    {
        return itsStackTable_.rawMaterialCheckIn(this, rm, uq);
    }
 
    public Boolean checkOut(RawMaterial rm, UnitQuantity uq)
    {
        return itsStackTable_.rawMaterialCheckOut(this, rm, uq);
    }
 
    public Boolean closeWareHouse(){return Boolean.FALSE;}
}
