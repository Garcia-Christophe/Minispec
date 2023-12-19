package metaModel;

import java.util.ArrayList;
import java.util.List;

public class Entity implements MinispecElement {

	private String name;
	private List<Attribute> attributes;

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

	public void accept(Visitor v) {
		for (Attribute attr : attributes) {
			attr.accept(v);
		}

		v.visitEntity(this);
	};

}
