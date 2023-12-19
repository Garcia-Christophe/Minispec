package metaModel;

import java.util.ArrayList;
import java.util.List;

public class Entity implements MinispecElement {

	private String name;
	private List<Attribute> attributes;
	private String parentClassName;

	public Entity() {
		this.attributes = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public void accept(Visitor v) {
		for (Attribute attr : attributes) {
			attr.accept(v);
		}

		v.visitEntity(this);
	};

}
