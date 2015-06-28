/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;

import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import oims.dataBase.DataBaseManager;
import oims.dataBase.Db_db;
import oims.dataBase.Db_table;
import oims.support.util.Db_publicColumnAttribute;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ezouyyi
 */
public class Db_dataBase_MYSQLTest {
    
    public Db_dataBase_MYSQLTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getDataBaseName method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testGetDataBaseName() {
        System.out.println("getDataBaseName");
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        String expResult = "";
        String result = instance.getDataBaseName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of initDataBase method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testInitDataBase() {
        System.out.println("initDataBase");
        String user = "";
        String pw = "";
        String host = "";
        String port = "";
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        instance.initDataBase(user, pw, host, port);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of createDb method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testCreateDb() {
        System.out.println("createDb");
        Db_db db = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Db_db expResult = null;
        Db_db result = instance.createDb(db);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of createTable method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testCreateTable() {
        System.out.println("createTable");
        Db_table table = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Db_db expResult = null;
        Db_db result = instance.createTable(table);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of insertRecord method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testInsertRecord() {
        System.out.println("insertRecord");
        Db_table.TableEntry entry = null;
        Db_table table = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Boolean expResult = null;
        Boolean result = instance.insertRecord(entry, table);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of close method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testClose() {
        System.out.println("close");
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of selectDb method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testSelectDb() {
        System.out.println("selectDb");
        Db_db db = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Db_db expResult = null;
        Db_db result = instance.selectDb(db);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of currentDb method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testCurrentDb() {
        System.out.println("currentDb");
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Db_db expResult = null;
        Db_db result = instance.currentDb();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of select method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testSelect() {
        System.out.println("select");
        List<String> selectList = null;
        Db_table.TableEntry entry_eq = null;
        Db_table.TableEntry entry_gr = null;
        Db_table.TableEntry entry_sml = null;
        Db_table table = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        ResultSet expResult = null;
        ResultSet result = instance.select(selectList, entry_eq, entry_gr, entry_sml, table);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of update method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testUpdate() {
        System.out.println("update");
        Db_table.TableEntry updateEntry = null;
        Db_table.TableEntry entry_eq = null;
        Db_table.TableEntry entry_gr = null;
        Db_table.TableEntry entry_sml = null;
        Db_table table = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Boolean expResult = null;
        Boolean result = instance.update(updateEntry, entry_eq, entry_gr, entry_sml, table);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of delete method, of class Db_dataBase_MYSQL.
     
    @Test
    public void testDelete() {
        System.out.println("delete");
        Db_table.TableEntry entry_eq = null;
        Db_table.TableEntry entry_gr = null;
        Db_table.TableEntry entry_sml = null;
        Db_table table = null;
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Boolean expResult = null;
        Boolean result = instance.delete(entry_eq, entry_gr, entry_sml, table);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of generateQuery_where method, of class Db_dataBase_MYSQL.
     */
    @Test
    public void testGenerateQuery_where() {
        System.out.println("generateQuery_where");
        DataBaseManager DataBaseMangerStub = new DataBaseManager();
        Db_dataBase_MYSQL instance = new Db_dataBase_MYSQL();
        Db_table Db_tableStub = new Db_table("testTable", DataBaseMangerStub);
        
        Db_tableStub.registerColumn("c1", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        Db_tableStub.registerColumn("c2", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        Db_tableStub.registerColumn("c3", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        Db_tableStub.registerColumn("c4", Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
        
        
        Db_table.TableEntry entry_eq = Db_tableStub.generateTableEntry();
        Map<String, String> value_eq = Maps.newHashMap();
        value_eq.put("c4", "0");
        value_eq.put("c1", "1");
        entry_eq.fillInEntryValues(value_eq);
        Db_table.TableEntry entry_gr = null;
        Db_table.TableEntry entry_sml = null;
        
        String expResult = " where c4=0 and c1=1";
        String result = instance.generateQuery_where(entry_eq, entry_gr, entry_sml);
        System.out.println(result);
        assertEquals(expResult, result);
        
        Db_table.TableEntry entry_eq2 = Db_tableStub.generateTableEntry();
        Map<String, String> value_eq2 = Maps.newHashMap();
        value_eq2.put("c4", "0");
        entry_eq2.fillInEntryValues(value_eq2);
        Db_table.TableEntry entry_gr2 = null;
        Db_table.TableEntry entry_sml2 = null;
        
        String expResult2 = " where c4=0";
        String result2 = instance.generateQuery_where(entry_eq2, entry_gr2, entry_sml2);
        System.out.println(result2);
        assertEquals(expResult2, result2);
               
        Db_table.TableEntry entry_eq3 = Db_tableStub.generateTableEntry();
        Map<String, String> value_eq3 = Maps.newHashMap();
        value_eq3.put("c4", "0");
        entry_eq3.fillInEntryValues(value_eq2);
        Db_table.TableEntry entry_gr3 = Db_tableStub.generateTableEntry();
        Map<String, String> value_gr3 = Maps.newHashMap();
        value_gr3.put("c2", "1");
        entry_gr3.fillInEntryValues(value_gr3);
        Db_table.TableEntry entry_sml3 = null;
        
        String expResult3 = " where c4=0 and c2>1";
        String result3 = instance.generateQuery_where(entry_eq3, entry_gr3, entry_sml3);
        System.out.println(result3);
        assertEquals(expResult3, result3);
        
        Db_table.TableEntry entry_eq4 = Db_tableStub.generateTableEntry();
        Map<String, String> value_eq4 = Maps.newHashMap();
        value_eq4.put("c4", "0");
        entry_eq4.fillInEntryValues(value_eq4);
        Db_table.TableEntry entry_gr4 = Db_tableStub.generateTableEntry();
        Map<String, String> value_gr4 = Maps.newHashMap();
        value_gr4.put("c2", "1");
        entry_gr4.fillInEntryValues(value_gr4);
        Db_table.TableEntry entry_sml4 = Db_tableStub.generateTableEntry();
        Map<String, String> value_sml4 = Maps.newHashMap();
        value_sml4.put("c1", "1");
        entry_sml4.fillInEntryValues(value_sml4);
        
        String expResult4 = " where c4=0 and c2>1 and c1<1";
        String result4 = instance.generateQuery_where(entry_eq4, entry_gr4, entry_sml4);
        System.out.println(result4);
        assertEquals(expResult4, result4);
    }
    
}
