public class Num extends Expression {

	private double number;

	public Num(double pNum) throws ParseException {
		this.number = pNum;
	}

	@Override
	public void prettyPrint() {
		if (number >= 0)
			System.out.print(number);
		else
			System.out.print("(" + number + ")");
	}

	@Override
	public double getNum() {
		return number;
	}
	
	@Override
	public void initialiseTypes() throws ParseException {
		this.myType =  EnumType.NUM;
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		
		this.initialiseTypes();
	}
	
	public void setValue(Double pNumber) {
		this.number = pNumber;
	}

	@Override
	public void execute() throws ParseException {
	}
}
