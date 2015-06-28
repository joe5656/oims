/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.dataBase.MYSQL;

import oims.support.util.Db_publicColumnAttribute;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ezouyyi
 */
public class Db_columnTypeMapper_MYSQLTest {
    
    public Db_columnTypeMapper_MYSQLTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of MapAttribute method, of class Db_columnTypeMapper_MYSQL.
     */
    @Test
    public void testMapAttribute() {
        System.out.println("MapAttribute");
        Db_publicColumnAttribute.ATTRIBUTE_NAME name1 = Db_publicColumnAttribute.ATTRIBUTE_NAME.BIT;
        Db_publicColumnAttribute.ATTRIBUTE_NAME name2 = Db_publicColumnAttribute.ATTRIBUTE_NAME.YEAR;
        Db_columnTypeMapper_MYSQL instance = new Db_columnTypeMapper_MYSQL();
        String expResult1 = "BIT ";
        String expResult2 = "YEAR ";
        String result1 = instance.MapAttribute(name1);
        assertEquals(expResult1, result1);
        String result2 = instance.MapAttribute(name2);
        assertEquals(expResult2, result2);
    }
}
