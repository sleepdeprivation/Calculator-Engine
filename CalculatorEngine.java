import java.util.ArrayList;
import java.util.Stack;


public class CalculatorEngine {


    //holds the user input	
	private ArrayList<PriorityToken> input = new ArrayList<PriorityToken>();

    //tells the calculator what to do in various circumstances... -1 = error
	private int state = 0;

    //the result of the calculation performed after converting to infix/postfix
	double result = -1;

    //the memory register used by the M keys
	double register;
	
	private String ERRORMESSAGE; //not really used
	
	
    /*
        Static fields to hold commonly used operators,
        A static function to generate a token that contains a number,
        and some static functions to do operations
    */
	public static class PTOKENS{
		public static OperatorToken plus = new OperatorToken(3,3,'+');
		public static OperatorToken minus = new OperatorToken(4,4,'-');
		public static OperatorToken unaryMinus = new OperatorToken(7,7,'-', OPERATOR_TYPES.UNARY);
		public static OperatorToken div = new OperatorToken(5,5,'/');
		public static OperatorToken times = new OperatorToken(6,6,'*');
		public static OperatorToken fact = new OperatorToken(7,7,'!', OPERATOR_TYPES.UNARY);
		public static OperatorToken exp = new OperatorToken(8,8,'^');
		//unimplemented... for the future
		public static OperatorToken lParen = new OperatorToken(-10,10,'(');
		public static OperatorToken rParen = new OperatorToken(1,1,')');
		
		public static OperandToken digit(double i){
			return new OperandToken(-1,-1,(double) i);
		}
		
		public static Double doBinOp(double rOperand, double lOperand, char c){
			if (c == '+') 
				return lOperand + rOperand;
			else if (c == '-') 
				return lOperand - rOperand;
			else if (c == '*') 
				return lOperand * rOperand;
			else if (c == '/') {
					return lOperand / rOperand;
			}else if (c == '^')
				return Math.pow(lOperand, rOperand);
			else
				return Double.NaN;
		}
		
		public static double fact(int S){
			//System.out.println("FACTORIAL " + S);
			int pr = 1;
			for(int ii = S; ii > 1; ii--){
				pr *= ii;
			}
			return pr;
		}
		public static double doMonOp(double operand, char c){
			if(c == '!')
				return fact((int) operand);
			else if(c == '-')
				return -((int) operand);
			else
				return -((int) operand); //to stop compiler complaints
		}
	}
	
    /*
        Add various tokens to the input list
    */
	public void add() {
		input.add(PTOKENS.plus);
	}

	public void subtract() {
		if(
			input.size() == 0 			||
			input.get(input.size()-1)
				instanceof OperatorToken
		){
			input.add(PTOKENS.unaryMinus);
		}else{
			input.add(PTOKENS.minus);
		}
	}

	public void multiply() {
		input.add(PTOKENS.times);
	}

	public void divide() {
		input.add(PTOKENS.div);
	}

	public void digit(double i) {
		if(
			input.size() != 0 && 
			input.get(input.size()-1) instanceof OperandToken
		){
			((OperandToken) input.get(input.size()-1)).addDigit(i);
		}else{
			input.add(PTOKENS.digit(i));
		}
	}

	public void fact() {
		input.add(PTOKENS.fact);
	}

	public void exp() {
		input.add(PTOKENS.exp);
	}

	public void lParen() {
		input.add(PTOKENS.lParen);
	}

	public void rParen() {
		input.add(PTOKENS.rParen);
	}

    //set the flag on the last operand to include a decimal
	public void decimal(){
		if(
			input.size() == 0 || 
			!(input.get(input.size()-1) instanceof OperandToken)
		){
			input.add(PTOKENS.digit(0));
		}
		((OperandToken) input.get(input.size()-1)).addDecimal();
	}
    
    //make an arraylist of priority tokens in infix form
    //into an arraylist of priority tokens into postfix form
	private ArrayList<PriorityToken> makePostfix(){
		try{
			Stack<PriorityToken> stack = new Stack<PriorityToken>();
			ArrayList<PriorityToken> out = new ArrayList<PriorityToken>();
			PriorityToken current;
			for(int ii = 0; ii < input.size(); ii++){
				current = input.get(ii);
				//System.out.println("hello?");
				//System.out.println(current);
				if(current instanceof OperandToken){
					//System.out.println("operand " + current + " being added to output");
					out.add(current);
				}else if(current instanceof OperatorToken){
					//System.out.println("operator " + current + " ...");
					if((char)current.getRepresentation() == ')'){
						//System.out.println("\t dumping stack until lparen");
						while(!stack.empty() && stack.peek() != PTOKENS.lParen){
							//System.out.println("\t \t dumping ..." + stack.peek());
							out.add(stack.pop());
						}
						//System.out.println("\t dump complete... deleting lparen");
						stack.pop(); //throw away lparen
					}else{
						//System.out.println("\t regular operator... dumping stack until higher precedence");
						while(!stack.empty() && stack.peek().instackPriority >= current.priority){
							//System.out.println("\t \t popping " + stack.peek());
							PriorityToken t = stack.pop();
							out.add(t);
						}
						//System.out.println("\t \t pushing onto stack " + current);
						stack.push(current);
					}
				}
			}
			while(!stack.isEmpty()){
				PriorityToken t = stack.pop();
				//System.out.println("Final stack dump " + t);
				out.add(t);
			}
			//System.out.println("printing result");
			//for(int ii = 0; ii < out.size(); ii++){
				//System.out.print(out.get(ii).getRepresentation());
				//System.out.print(" ");
			//}
			//System.out.println("");
			return out;
		}catch(java.util.EmptyStackException e){
			System.err.println("POSTFIX EMPTY STACK EXCEPTION");
			state = -1;
			result = Double.NaN;
			ERRORMESSAGE = "Malformed Expression!";
			return null;
		}
	}
	
    //take an arraylist of prioritytokens in postfix form and evaluate it
	private double evaluatePostfix(ArrayList<PriorityToken> pfExp){
		try{
			Stack<Double> stack = new Stack<Double>();
			PriorityToken current;
			for(int ii = 0; ii < pfExp.size(); ii++){
				current = pfExp.get(ii);
				if(current instanceof OperandToken){
					stack.push((Double)current.getRepresentation());
				}else if(current instanceof OperatorToken){
					if(((OperatorToken) current).opType == OPERATOR_TYPES.BINARY){
						stack.push(PTOKENS.doBinOp(stack.pop(),
										stack.pop(),
										(char)current.getRepresentation()));
					}else{
						stack.push(PTOKENS.doMonOp(stack.pop(),
								(char)current.getRepresentation()));
					}
				}
				//System.out.println(stack.peek());
			}
			//System.out.println(stack.peek());
			return stack.pop();
		}catch(ArithmeticException e){
			System.err.println("ARITHMETIC ERROR");
			state = -1;
			ERRORMESSAGE = "ERROR: DIVISION BY 0";
			return Double.NaN;
		}catch(java.util.EmptyStackException e){
			System.err.println("EMPTY STACK ERROR");
			state = -1;
			result = Double.NaN;
			ERRORMESSAGE = "Malformed Expression!";
			return Double.NaN;
		}
	}

    //convert the input list and into postfix and evaluate
	public void compute() {
		//System.out.println("postfix!!!");
		ArrayList<PriorityToken> n = makePostfix();
		if(n != null && state != -1){
			result = evaluatePostfix(n);
			state = 1;
		}
	}

    //clear out the input and reset our state
	public void clear() {
		input = new ArrayList<PriorityToken>();
		state = 0;
	}

    //use the toString methods to display input
	public String display() {
		if(state == 0){
			String output = "";
			for(int ii = 0; ii < input.size(); ii++){
				output += input.get(ii).toString();
			}
			return output;
		}else if(state == -1){
			return ERRORMESSAGE;
		}else{
			return Double.toString(result);
		}
	}

    //place something in memory register
	public void mPut() {
		if(input.size() != 0){
			compute();
		}
		if(!Double.isNaN(this.result)){
			register = this.result;
			//System.out.println("placed in register :"+register+" clearing...");
			clear();
		}else{
			state = -1;
			ERRORMESSAGE = "ERROR: BAD VALUE";
		}
	}

    //clear something from memory register
	public void mClear() {
		register = Double.NaN;
	}

    //retrieve something from memory register
	public void mGet() {
		if(register != Double.NaN){
			//System.out.println("Not a NaN "+register);
			digit(register);
		}
	}

}
