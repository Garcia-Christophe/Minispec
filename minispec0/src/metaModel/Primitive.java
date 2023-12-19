package metaModel;

public class Primitive implements MinispecElement {

	private String name;
	private String type;
	private String packageName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	};

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void accept(Visitor v) {
		v.visitPrimitive(this);
	}

}
