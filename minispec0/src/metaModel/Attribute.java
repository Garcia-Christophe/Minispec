package metaModel;

public class Attribute implements MinispecElement {

	TypeDesc type;
	String name;
	String initialValue;

	public TypeDesc getType() {
		return type;
	}

	public void setType(TypeDesc type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public void accept(Visitor v) {
		type.accept(v);
		v.visitAttribute(this);
	};

}
