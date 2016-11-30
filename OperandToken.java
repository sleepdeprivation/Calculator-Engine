/*
    Represents an operand
*/
public class OperandToken extends PriorityToken{

	OperandToken(int is, int p, Double c) {
		super(is, p, c);
	}

    /*
        Operands are doubles
    */
	public Double getRepresentation(){
		//System.out.println(representation);
		return (Double)representation;
	}
	
    //a flag that lets you add decimals
	protected boolean haveDecimal = false;
	protected int howManyDecimals = 1;
	
    //add a digit to this operand taking into account whether we've seen a decimal
	public void addDigit(double i){
		if(!haveDecimal){
			i += (Double)representation*10;
		}else{
			i = (i/(Math.pow(10 , howManyDecimals))) + (Double)representation;
			howManyDecimals++;
		}
		representation = i;
	}
	
    //we saw a decimal
	public void addDecimal(){
		haveDecimal = true;
	}
	
}
