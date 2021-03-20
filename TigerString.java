public class TigerString extends Expression {

	String stringVar;

	public TigerString(String pStringVar) throws ParseException {

		if (pStringVar.startsWith("\"") && pStringVar.endsWith("\"")) // '"abcd"' devient 'abcd'
			this.stringVar = pStringVar.substring(1, pStringVar.length() - 1);
		else
			this.stringVar = pStringVar;
	}

	@Override
	public void prettyPrint() {
		System.out.print("\"" + stringVar + "\""); // \" <=> "
	}

	@Override
	public String getString() {
		return stringVar;
	}

	@Override
	public void initialiseTypes() throws ParseException {
		this.myType =  EnumType.STRING;
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		
		this.initialiseTypes();
	}
	
	public void setString(String pStringVar) {
		this.stringVar = pStringVar;
	}

	@Override
	public void execute() throws ParseException {
	}
}
