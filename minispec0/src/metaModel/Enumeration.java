package metaModel;

import java.util.ArrayList;
import java.util.List;

public class Enumeration implements MinispecElement {

	private String name;
	private List<String> items;

	public Enumeration() {
		items = new ArrayList<>();
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void accept(Visitor v) {
		v.visitEnumeration(this);
	};

}
