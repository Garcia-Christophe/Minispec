package metaModel;

public class Attribute implements MinispecElement {

	TypeDesc type;
	String name;

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

	public void accept(Visitor v) {
		type.accept(v);
		v.visitAttribute(this);
	};

}
