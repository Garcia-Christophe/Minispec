package metaModel;

public class NamedType extends TypeDesc {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void accept(Visitor v) {
		v.visitNamedType(this);
	}
}
