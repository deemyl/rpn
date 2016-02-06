package com.test.anz;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
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
	private static final String OPERATOR_STR = "operator";
	private static final String SPACE_STR = " ";
	private static final String POSITION_STR = "position:";
	private static final String OPEN_BRACKET_STR = "(";
	private static final String CLOSE_BRACKET_STR = ")";
	private static final String STACK_STR = "stack:";
	private static final String OPERAND_REGEX = "[-+*/]";
	private static final String COMMAND_REGEX = "(?i:sqrt)|(?i:undo)|(?i:clear)";
	private static final String REAL_NUMBERS_REGEX = "^\\d+(\\.\\d+)?$";	//TODO:NEED TO TEST FOR ALL REAL NUMBERS
	
	protected Stack<String> stack = new Stack<String>();
	
	public static void main( String[] args )
    {
        App rpnApp = new App();
        rpnApp.runCalculator();
    }
    
	protected void runCalculator() {
		List<String> completeInputList = new ArrayList<String>();
        Scanner sc = new Scanner(System.in);
        while (true) {
	        int size = completeInputList.size();
	        String input = sc.nextLine().trim();
	        List<String> inputList = validateInputString(input);
	        completeInputList.addAll(inputList);
	        createStack(completeInputList, size);	        
        }
	}
	
    protected List<String> validateInputString(String input) {
    	List<String> inputList = new ArrayList<String>();
    	if(StringUtils.isEmpty(input.trim())) {
    		printIncorrectParameterMsg(" ", 0);
    	} else {
    		inputList = Arrays.asList(input.split(" "));
	    	if(inputList.isEmpty()) { 
	    		printIncorrectParameterMsg(inputList.get(0), 0);
	    	}
    	}
    	return inputList;
    }
    
    protected void createStack(List<String> inputList, int index) {
    	String prevValinStack = null;
    	String currValinStack = null;
    	for(int i = index; i < inputList.size(); i++) {
    		int pointerPos = i - index;
    		String currVal = inputList.get(i);
    		if(!validateEntry(currVal)) {
    			printIncorrectParameterMsg(currVal, pointerPos);
    			break;
    		}
    		if(!isNumber(currVal)) {
    			currValinStack = peek();
    			if(currValinStack == null) {
    				printIncorrectParameterMsg(currVal, pointerPos);
    				break;
    			}    		
	    		if(isOperand(currVal)) {
	    			currValinStack = stack.pop();
	    			prevValinStack = peek();
    				if(prevValinStack == null) {
    					printIncorrectParameterMsg(currVal, pointerPos);
    					stack.push(currValinStack);
        				break;
    				} else {
    					stack.push(doOperation(stack.pop(), currValinStack, currVal));
    					continue;
    				}	
	    		} else if(isSquareRootCommand(currVal)) {
	    			currValinStack = stack.pop();
	    			String result = squareroot(currValinStack);
    				stack.push(result);
	    		} else if(isUndoCommand(currVal)) {
	    			String prevVal = inputList.get(i-1);
	    			if(isNumber(prevVal)) {
	    				stack.pop();
	    			} else if(isOperand(prevVal)) {
	    				if(i >= 3) {
	    					stack.pop();
	    					stack.push(inputList.get(i-3));
	    					stack.push(inputList.get(i-2));
	    				} else {
	    					//incorrect error
	    					break;
	    				}
	    			} else if(isSquareRootCommand(prevVal)) {
	    				stack.pop();
	    				stack.push(inputList.get(i-2));
	    			} else if(isUndoCommand(prevVal)) {
	    				if(peek() == null) {
	    					printIncorrectParameterMsg(currVal, pointerPos);
	    					break;
	    				} else {
	    					stack.pop();
	    				}
	    			} else { 
	    				continue;
	    			}
	    		} else if(isClearCommand(currVal)) {
	        		stack.clear();
	        	} else {
	        		//incorrect command it is not number or the known operations
	        	}
    		} else if(isNumber(currVal)) {
    			stack.push(currVal);
    		} else {
    			System.out.println("ERROR");
    		}
    	}
    	printStack();
    }
    
    protected boolean validateEntry(String val) {
    	return isNumber(val) || isKnownOperation(val);
    }
    
    protected boolean isKnownOperation(String val) {
    	return val.matches(OPERAND_REGEX + "|" + COMMAND_REGEX);
    }
    
    protected boolean isOperand(String val) {
    	return val.matches(OPERAND_REGEX);
    }
    
    protected boolean isSquareRootCommand(String val) {
    	return val.matches("(?i:sqrt)");
    }
    
    protected boolean isClearCommand(String val) {
    	return val.matches("(?i:clear)");
    }
    
    protected boolean isUndoCommand(String val) {
    	return val.matches("(?i:undo)");
    }
    
    protected boolean isNumber(String val) {
    	return val.matches(REAL_NUMBERS_REGEX);
    }
    
    private void printIncorrectParameterMsg(String val, int position) {
    	if(position == 0) {
    		position = 1;
    	} else {
    		position = position*2 + 1;	//add the space position
    	}
    	StringBuffer sb = new StringBuffer();
		sb.append(OPERATOR_STR).append(SPACE_STR).append(val).append(SPACE_STR);
		sb.append(OPEN_BRACKET_STR).append(POSITION_STR).append(SPACE_STR).append(position);
		sb.append(CLOSE_BRACKET_STR).append(INSUFF_PARAMETERS_STR);
		System.out.println(sb.toString());
    }
    
    private void printStack() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(STACK_STR).append(SPACE_STR);
    	for (int i = 0; i < stack.size(); i++) {
    		sb.append(stack.get(i)).append(SPACE_STR);    		
    	}
    	System.out.println(sb.toString().trim());
    }
    
    protected String peek() {
    	try {
    		return stack.peek();
    	} catch(EmptyStackException e) {
    		//since there is nothing in the stack
    		return null;
    	}
    }
    
    protected String squareroot(String val) {
    	return (new BigDecimal(Math.sqrt(convertString(val).doubleValue()), new MathContext(15))).toString();
    }
    
    protected BigDecimal convertString(String val) {
    	return new BigDecimal(val, new MathContext(15));
    }
    
    protected String doOperation(String val1, String val2, String operation) {   	
    	switch(operation) {
    		case "+":		return convertString(val1).add(convertString(val2)).toString();
    		case "-":		return convertString(val1).subtract(convertString(val2)).toString();
    		case "/":		return convertString(val1).divide(convertString(val2)).toString();
    		case "*":		return convertString(val1).multiply(convertString(val2)).toString();
    			
    		default:		return null;
    	}
    }
}
