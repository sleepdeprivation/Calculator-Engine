/*
    Represents an operator
*/
public class OperatorToken extends PriorityToken{

    //is in unary or binary?
	public OPERATOR_TYPES opType = OPERATOR_TYPES.BINARY;

	OperatorToken(int is, int p, Character c) {
		super(is, p, c);
	}

	OperatorToken(int is, int p, Character c, OPERATOR_TYPES type) {
		super(is, p, c);
		opType = type;
	}

    //this is an operator so its representation is a character
	public Character getRepresentation(){
		return (Character)representation;
	}
	
}
