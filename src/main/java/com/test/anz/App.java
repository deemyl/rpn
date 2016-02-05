package com.test.anz;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;


/**
 * Hello world!
 *
 */
public class App 
{
	private static final String INSUFF_PARAMETERS_STR = ": insufficient parameters";
	private static final String MSG_INSTRUCTION = "Please enter digits and operands such as +,-,*,/,sqrt,clear,undo with spaces between.\n";
	private static final String OPERATOR_STR = "operator";
	private static final String SPACE_STR = " ";
	private static final String POSITION_STR = "position";
	private static final String OPEN_BRACKET_STR = "(";
	private static final String CLOSE_BRACKET_STR = ")";
	private static final String STACK_STR = "stack:";
	private static final String OPERAND_REGEX = "[-+*/]";
	private static final String REAL_NUMBERS_REGEX = "\\d+";	//TODO:NEED TO TEST FOR ALL REAL NUMBERS
	
	private static Stack<String> stack = new Stack<String>();
	
	public static void main( String[] args )
    {
        App rpnApp = new App();
        rpnApp.validateInput();
        
    }
    
    private List<String> validateInput() {
    	String input = null;
    	Scanner in = null;
    	while(StringUtils.isEmpty(input)) {
    		System.out.println(MSG_INSTRUCTION);
        	in = new Scanner(System.in);
            input = in.nextLine().trim();
    	}
    	in.close();
        List<String> inputList = Arrays.asList(input.split(" "));
    	if(inputList.isEmpty() || inputList.size() <= 1) {
    		printIncorrectParameterMsg(inputList.get(0), 0);
    		System.exit(-1);
    	}
    	return inputList;
    }
    
    private void createStack(List<String> inputList) {
    	for(int i = 0; i < inputList.size(); i++) {
    		//if(inputList.get(i) )
    		//stack.push(inputList.get(i));
    	}
    }
    
    private boolean isOperand(String val) {
    	return val.matches(OPERAND_REGEX);
    }
    
    private boolean isNumber(String val) {
    	return val.matches(REAL_NUMBERS_REGEX);
    }
    
    private void printIncorrectParameterMsg(String val, int position) {
    	StringBuffer sb = new StringBuffer();
		sb.append(OPERATOR_STR).append(SPACE_STR).append(val).append(SPACE_STR);
		sb.append(OPEN_BRACKET_STR).append(POSITION_STR).append(SPACE_STR).append(position);
		sb.append(CLOSE_BRACKET_STR).append(INSUFF_PARAMETERS_STR);
		System.out.println(sb.toString());
    }
    
    private void printStack(Stack<String> stck) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(STACK_STR).append(SPACE_STR);
    	for (int i = 0; i <stck.size(); i++) {
    		sb.append(stck.get(i)).append(SPACE_STR);    		
    	}
    	System.out.println(sb.toString().trim());
    }
    
    private void pop() {
    	
    }
    
    private void multiply() {
    	
    }
    
    private void divide() {
    	
    }
    
    private void add() {
    	
    }
    
    private void subtract() {
    	
    }
    
    private void undo() {
    	
    }
    
    private void clear() {
    	
    }
    
    private void squareroot() {
    	
    }
}
