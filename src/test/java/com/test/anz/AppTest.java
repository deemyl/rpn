package com.test.anz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    	String pattern = "[-+*/]|(?i:sqrt)|(?i:undo)|(?i:clear)";
    	assertFalse("111".matches(pattern));
    	assertFalse("+-".matches(pattern));
    	assertFalse(" ".matches(pattern));
    	assertFalse("sdf".matches(pattern));
    	assertFalse("1*".matches(pattern));
    	assertTrue("+".matches(pattern));
    	assertTrue("-".matches(pattern));
    	assertTrue("/".matches(pattern));
    	assertTrue("*".matches(pattern));
    	assertFalse("[".matches(pattern));
    	assertTrue("sqrt".matches(pattern));
    	assertTrue("SQRT".matches(pattern));
    	assertFalse("sqrt+".matches(pattern));
    	assertFalse("sqrtclear".matches(pattern));
    	
    }
    
    public void testIsNumber() {
    	String pattern = "^\\d+(\\.\\d+)?$";
    	assertTrue("111".matches(pattern));
    	assertFalse("1*".matches(pattern));
    	assertFalse("1a1".matches(pattern));
    	assertTrue("1.11111111".matches(pattern)); 
    	//TODO: need more testing for input with diff precision -- what is allowed???
    }
    
    public void testValidateInputString() {
    	assertTrue(rpnApp.validateInputString(" ").isEmpty());
    	assertTrue(rpnApp.validateInputString("").isEmpty());
    	assertFalse(rpnApp.validateInputString("1*").isEmpty());
    	assertTrue(rpnApp.validateInputString("1*").size() == 1);
    }
    
    public void testValidateEntry() {
    	assertFalse(rpnApp.validateEntry("1*"));
    	assertFalse(rpnApp.validateEntry("**"));
    	assertFalse(rpnApp.validateEntry("*/"));
    	assertFalse(rpnApp.validateEntry("$%"));
    	assertFalse(rpnApp.validateEntry("sqrtclear"));
    	assertTrue(rpnApp.validateEntry("*"));
    	assertTrue(rpnApp.validateEntry("145.23253"));
    	assertTrue(rpnApp.validateEntry("sqrt"));
    	assertTrue(rpnApp.validateEntry("clear"));
    	assertTrue(rpnApp.validateEntry("undo"));
    }
    
    public void testDoOperation() {
    	String expResult = "4";
    	assertNull(rpnApp.doOperation("1", "2", "%"));
    	assertTrue(rpnApp.doOperation("2", "2", "+").equals(expResult));
    }
    
    public void testConvertString() {
    	String testStr = "5";
    	BigDecimal expRes = new BigDecimal(testStr);
    	assertEquals(expRes, rpnApp.convertString(testStr));
    	
    	testStr = "5.666666666666666666666666";
    	assertEquals(new BigDecimal("5.66666666666667"), rpnApp.convertString(testStr));		//testing the precision upto 15
    }
    
    public void testCreateStack() {
    	List<String> completeList = new ArrayList<String>();
    	List<String> inputList = createSampleInputList("32", "20", "-");
    	String result = "12";
    	completeList.addAll(inputList);
    	rpnApp.createStack(completeList, 0);
    	assertTrue(rpnApp.stack.size() == 1);
    	assertTrue(rpnApp.stack.peek().equals(result));
    	
    	inputList = createSampleInputList("undo");
    	completeList.addAll(inputList);
    	rpnApp.createStack(completeList, 3);
    	assertTrue(rpnApp.stack.size() == 2);
    	assertTrue(rpnApp.stack.get(0).equals("32"));
    	assertTrue(rpnApp.stack.get(1).equals("20"));
    	
    	inputList = createSampleInputList("clear");
    	completeList.addAll(inputList);
    	rpnApp.createStack(completeList, 4);
    	assertTrue(rpnApp.stack.size() == 0);
    	
    	completeList.clear();
    	rpnApp.stack.clear();
    	inputList = createSampleInputList("1", "2", "+", "undo", "*", "undo");
    	completeList.addAll(inputList);
    	rpnApp.createStack(completeList, 0);
    	assertTrue(rpnApp.stack.size() == 2);
    	
    	completeList.clear();
    	rpnApp.stack.clear();
    	inputList = createSampleInputList("1", "2", "*", "undo", "3", "+", "undo");
    	completeList.addAll(inputList);
    	rpnApp.createStack(completeList, 0);
    	assertTrue(rpnApp.stack.size() == 3);
    	
    	//TODO more testing for sqrt 
    	
    	//TODO more test for clear in conjection with other commands.
    }
    
    private List<String> createSampleInputList(String... strArr) {
    	if (strArr.length > 0) {
    		return (List<String>) Arrays.asList(strArr);
    	} 
    	return null;
    }
}
