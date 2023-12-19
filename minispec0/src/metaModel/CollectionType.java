package metaModel;

public class CollectionType extends TypeDesc {

	private int min;
	private int max;
	private TypeDesc elementType;
	private CollectionTypeEnum collectionTypeEnum;

	public CollectionType() {
		min = 0;
		max = Integer.MAX_VALUE;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public TypeDesc getElementType() {
		return elementType;
	}

	public void setElementType(TypeDesc elementType) {
		this.elementType = elementType;
	}

	public CollectionTypeEnum getCollectionTypeEnum() {
		return collectionTypeEnum;
	}

	public void setCollectionTypeEnum(CollectionTypeEnum collectionTypeEnum) {
		this.collectionTypeEnum = collectionTypeEnum;
	}

	@Override
	public void accept(Visitor v) {
		elementType.accept(v);
		v.visitCollectionType(this);
	}

}
