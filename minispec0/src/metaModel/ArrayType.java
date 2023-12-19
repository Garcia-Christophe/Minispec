package metaModel;

public class ArrayType extends TypeDesc {

	private int size;
	private TypeDesc elementType;

	public ArrayType() {
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public TypeDesc getElementType() {
		return elementType;
	}

	public void setElementType(TypeDesc elementType) {
		this.elementType = elementType;
	}

	@Override
	public void accept(Visitor v) {
		elementType.accept(v);
		v.visitArrayType(this);
	}

}
