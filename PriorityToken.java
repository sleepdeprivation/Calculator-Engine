
/*
 * PriorityToken has two priorities,
 * and a representation
 */
public class PriorityToken {
	public int instackPriority;
	public int priority;
	protected Object representation;
	
	PriorityToken(int is, int p, Object c){
		instackPriority = is;
		priority = p;
		representation = c;
	}
	
	public Object getRepresentation(){
		return representation;
	}
	
	@Override
	public String toString(){
		if(this instanceof OperandToken){
			if(((OperandToken)this).haveDecimal){
				return ((Double) representation).toString();
			}else if((Double)this.getRepresentation() == ((Double) this.getRepresentation()).intValue()){
				return ((Integer)
						((Double)representation)
							.intValue()
						).toString();
			}else{
				return ((Double)representation).toString();
			}
		}
		return representation.toString();
	}
	
}
