/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oims.support.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author freda
 */
public class CommonUnitTest {
    
    public CommonUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getUnitListStringChn method, of class CommonUnit.
     */
    @Test
    public void testGetUnitListStringChn() {
        System.out.println("getUnitListStringChn");
        String[] expResult = null;
        String[] result = CommonUnit.getUnitListStringChn();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getStanderizedUnitChn method, of class CommonUnit.
     */
    @Test
    public void testGetStanderizedUnitChn() {
        System.out.println("getStanderizedUnitChn");
        String[] expResult = null;
        String[] result = CommonUnit.getStanderizedUnitChn();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUnstanderizedUnitChn method, of class CommonUnit.
     */
    @Test
    public void testGetUnstanderizedUnitChn() {
        System.out.println("getUnstanderizedUnitChn");
        String[] expResult = null;
        String[] result = CommonUnit.getUnstanderizedUnitChn();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUnit method, of class CommonUnit.
     */
    @Test
    public void testGetUnit() {
        System.out.println("getUnit");
        CommonUnit instance = null;
        CommonUnit.UNITS expResult = null;
        CommonUnit.UNITS result = instance.getUnit();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUnitChanageFactor method, of class CommonUnit.
     */
    @Test
    public void testGetUnitChanageFactor() {
        System.out.println("getUnitChanageFactor");
        CommonUnit.UNITS changeTo = CommonUnit.UNITS.lit;
        CommonUnit instance = new CommonUnit("5升 瓶装");
        Double expResult = 5.0;
        Double result = instance.getUnitChanageFactor(changeTo);
        System.out.print(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUnitName method, of class CommonUnit.
     */
    @Test
    public void testGetUnitName() {
        System.out.println("getUnitName");
        CommonUnit instance = null;
        String expResult = "";
        String result = instance.getUnitName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
