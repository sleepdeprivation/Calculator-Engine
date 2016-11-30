
public class CalculatorTest {
	
	public CalculatorEngine engine = new CalculatorEngine();
	
	/*
	 * Just duplicated the stuff in the frontend
	 */
	public void tokenizeOne(char c){
		if (c == '+') 
			engine.add();
		else if (c == '-') 
			engine.subtract();
		else if (c == '*') 
			engine.multiply();
		else if (c == '/') 
			engine.divide();
		else if (c >= '0' && c <= '9') 
			engine.digit(c - '0');
		else if (c == '=') 
			engine.compute();
		else if (c == 'C') 
			engine.clear();
		else if (c == '!') 
			engine.fact();
		else if (c == '^'){
			engine.exp();
		/*
		 * Except for this stuff... Tested it manually and it appears to work
		else if (c == 'M'){
			System.out.println("memory button");
			char two = e.getActionCommand().charAt(1);
			System.out.println("second char" + two);
			if(two == '+')
				engine.mPut();
			else if(two == 'C')
				engine.mClear();
			else if(two == 'R')
				engine.mGet();
				*/
		}else if(c == '('){
			engine.lParen();
		}else if(c == ')'){
			engine.rParen();
		}else if(c == '.'){
			engine.decimal();
		}
	}

	//found the first bunch of these on stackexchange,
	//then I added a few at the bottom to test pow, fact and unary minus
	public String[] expressions =
		{
			"10-11",
			"9-10",
			"9-9-1",
			"2-3",
			"10-1",
			"4-3",
			"4+2",
			"3*6-7+2",
			"6*2+(5-3)*3-8",
			"(3+4)+7*2-1-9",
			"5-2+4*(8-(5+1))+9",
			"(8-1+3)*6-((3+7)*2)",
			"5-24*(8-(5+1))+9",
			"5-2^4*(8-(5!+1))+9",
			"5-2+-4*(8-(5+1))+9"};

	//Using java's built in parsing for testing
	public double[] answers =
		{ 	10-11,
			9-10,
			9-9-1,
			2-3,
			10-1,
			4-3,
			4+2,
			3*6-7+2,
			6*2+(5-3)*3-8,
			(3+4)+7*2-1-9,
			5-2+4*(8-(5+1))+9,
			(8-1+3)*6-((3+7)*2),
			5-24*(8-(5+1))+9,
			5-Math.pow(2, 4)*(8-((5*4*3*2*1)+1))+9,
			5-2+-4*(8-(5+1))+9
		};

	/*
	 * tokenize and calculate the strings and check them with what
	 * java system got
	 * then say if we won them all or not
	 */
	public static void main(String arg[]) {
		CalculatorTest t = new CalculatorTest();
		Boolean allClear = true;
		for(int EE =0; EE < t.expressions.length; EE++){
			String s = t.expressions[EE];
			double d = t.answers[EE];
			System.out.println(s);
			for(int ii =0; ii < s.length(); ii++){
				t.tokenizeOne(s.charAt(ii));
			}
			t.engine.compute();
			System.out.println("ENGINE RESULT:"+t.engine.display());
			System.out.println("JAVA RESULT  :"+d);
			System.out.println("SAME?" + (t.engine.result == d));
			allClear = allClear && t.engine.result == d;
			t.engine.clear();
		}
		if(allClear){
			System.out.println("All the tests were passed");
		}else{
			System.out.println("One or more tests failed!");			
		}
	 }
		
}
