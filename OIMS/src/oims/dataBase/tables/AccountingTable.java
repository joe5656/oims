/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.tables;

import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;

/**
 *
 * @author ezouyyi
 */
public class AccountingTable  extends Db_table{
    static public String AccountingTable()
    {
        return "AccountingTable";
    }
    
    public AccountingTable(DataBaseManager dbm)
    {
        super("AccountingTable", dbm, Table_Type.TABLE_TYPE_REMOTE);
        // recipt format: MaterialId1:MaterialName1:quantity1:unit1|.....|MaterialIdN:MaterialNameN:quantityN:unitN
        super.registerColumn("amount", Db_publicColumnAttribute.ATTRIBUTE_NAME.DOUBLE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        super.registerColumn("cat", Db_publicColumnAttribute.ATTRIBUTE_NAME.VARCHAR60, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
        super.registerColumn("id", Db_publicColumnAttribute.ATTRIBUTE_NAME.INTEGER, Boolean.TRUE, Boolean.TRUE,  Boolean.TRUE, null);
    }
}
