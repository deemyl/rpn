package com.test.anz;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private App rpnApp = new App();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testIsOperand() {
    	String pattern = "[-+*/]";
    	assertFalse("111".matches(pattern));
    	assertFalse("+-".matches(pattern));
    	assertFalse("".matches(pattern));
    	assertFalse(" ".matches(pattern));
    	assertFalse("sdf".matches(pattern));
    	assertTrue("+".matches(pattern));
    	assertTrue("-".matches(pattern));
    	assertTrue("/".matches(pattern));
    	assertTrue("*".matches(pattern));
    	
    }
    
    public void testIsNumber() {
    	String pattern = "\\d+";
    	assertTrue("111".matches(pattern));
    	assertFalse("1a1".matches(pattern));
    	assertTrue("1.11111111".matches(pattern)); //???? GOT TO FIX THIS PATTERN 
    }
}
