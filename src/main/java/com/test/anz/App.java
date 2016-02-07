package com.test.anz;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

/**
 * RPN Calculator - commandline tool
 *
 */
public class App {
	private static final String INSUFF_PARAMETERS_STR = ": insufficient parameters";
	private static final String OPERATOR_STR = "operator";
	private static final String SPACE_STR = " ";
	private static final String POSITION_STR = "position:";
	private static final String OPEN_BRACKET_STR = "(";
	private static final String CLOSE_BRACKET_STR = ")";
	private static final String STACK_STR = "stack:";
	private static final String OPERAND_REGEX = "[-+*/]";
	private static final String COMMAND_REGEX = "(?i:sqrt)|(?i:undo)|(?i:clear)";
	private static final String REAL_NUMBERS_REGEX = "^\\d+(\\.\\d+)?$";

	protected Stack<String> stack = new Stack<String>();

	public static void main(String[] args) {
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
		if (StringUtils.isEmpty(input.trim())) {
			printIncorrectParameterMsg(" ", 0);
		} else {
			inputList = Arrays.asList(input.split(" "));
			if (inputList.isEmpty()) {
				printIncorrectParameterMsg(inputList.get(0), 0);
			}
		}
		return inputList;
	}

	protected void createStack(List<String> inputList, int index) {
		String prevValinStack = null;
		String currValinStack = null;
		for (int i = index; i < inputList.size(); i++) {
			int pointerPos = i - index;
			String currVal = inputList.get(i);
			if (!validateEntry(currVal)) { // check if it is a valid entry
				printIncorrectParameterMsg(currVal, pointerPos);
				break;
			}
			if (!isNumber(currVal)) { // if it is not a number, then need to
										// peek at the stack to see there is a
										// number in the stack
				currValinStack = peek();
				if (currValinStack == null) { // if not valid, then end the loop
					printIncorrectParameterMsg(currVal, pointerPos);
					break;
				}
				if (isOperand(currVal)) { // if operand, then pop the last two
											// vals and do the operation
					currValinStack = stack.pop();
					prevValinStack = peek(); // first peek at the stack to see
												// if it exists
					if (prevValinStack == null) {
						printIncorrectParameterMsg(currVal, pointerPos);
						stack.push(currValinStack);
						break;
					} else {
						stack.push(doOperation(stack.pop(), currValinStack, currVal));
						continue;
					}
				} else if (isSquareRootCommand(currVal)) { // if it is
															// squareRoot - u
															// only need to pop
															// only one value
					currValinStack = stack.pop();
					stack.push(squareroot(currValinStack));

				} else if (isUndoCommand(currVal)) { // if it is undo command, u
														// need to see what
														// operation/number or
														// what command
					int status = handleUndoOperation(inputList, i, pointerPos);
					if (status == 0) {
						break;
					} else {
						continue;
					}
				} else if (isClearCommand(currVal)) {
					stack.clear();
				}
			} else if (isNumber(currVal)) {
				stack.push(currVal);
			} else {
				System.out.println("ERROR");
			}
		}
		printStack();
	}

	protected int handleUndoOperation(List<String> inputList, int i, int pointerPos) {
		String currVal = inputList.get(i);
		//String prevVal = inputList.get(i - 1);
		if (isNumber(currVal)) { // if prev is number, then just pop it
			stack.pop();
		} else if (isOperand(currVal)) { // if it is some operation, u need to
											// find two numbers before this
											// operation
			if (i >= 2) {
				boolean donePrevVal = false;		
				String op1 = inputList.get(i - 1);
				String op2 = inputList.get(i - 2);
				if (isNumber(op1) || isNumber(op2)) {
					stack.pop();
				}
				if (!isNumber(op1)) {
					handleUndoOperation(inputList, i - 1, pointerPos);
					donePrevVal = true;					//this is a pointer to check if prevVal has looked at and operation handled
				} 
				if(!isNumber(op2) && !donePrevVal) {		
					handleUndoOperation(inputList, i - 2, pointerPos);
				} 
				if (isNumber(op2)) {
					stack.push(op2);
				}
				if (isNumber(op1)) {
					stack.push(op1);
				}

			} else {
				printIncorrectParameterMsg(currVal, pointerPos);
				return 0;
			}
		} else if (isSquareRootCommand(currVal)) { 
			if (peek() == null) {
				printIncorrectParameterMsg(currVal, pointerPos);
				return 0;
			} else {										
				String op1 = inputList.get(i - 1);			
				if (isNumber(op1)) {						// if it is number, then only pop the sqrt result and and push the orig
					stack.pop();
					stack.push(op1);
				} else {
					if (handleUndoOperation(inputList, i - 1, pointerPos) == 1) {
						stack.push(squareroot(stack.pop()));
					}
				}
			}
		} else if (isUndoCommand(currVal)) { // if prev is also undo, then peek
												// to see if a number exists in
												// the stack
			if (peek() == null) {
				printIncorrectParameterMsg(currVal, pointerPos);
				return 0;
			} else {
				String op1 = inputList.get(i - 1);
				if (isUndoCommand(op1)) {
					stack.pop();
				} else {
					handleUndoOperation(inputList, i - 1, pointerPos);
				}
			}
		}
		return 1;
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
		if (position == 0) {
			position = 1;
		} else {
			position = position * 2 + 1; // add the space position
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
		} catch (EmptyStackException e) {
			// since there is nothing in the stack
			return null;
		}
	}

	protected String squareroot(String val) {
		return convertBigDecimal(new BigDecimal(Math.sqrt(convertString(val).doubleValue()), new MathContext(15)));
	}

	protected BigDecimal convertString(String val) {
		return new BigDecimal(val, new MathContext(15));
	}

	protected String convertBigDecimal(BigDecimal decimal) {		//TODO Need to test this
		//return decimal.setScale(10, RoundingMode.CEILING).toString();		
		return decimal.toString();
	}
	
	protected String doOperation(String val1, String val2, String operation) {
		switch (operation) {
		case "+":
			return convertBigDecimal(convertString(val1).add(convertString(val2)));
		case "-":
			return convertBigDecimal(convertString(val1).subtract(convertString(val2)));
		case "/":
			return convertBigDecimal(convertString(val1).divide(convertString(val2)));
		case "*":
			return convertBigDecimal(convertString(val1).multiply(convertString(val2)));

		default:
			return null;
		}
	}
}
