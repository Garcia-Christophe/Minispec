package metaModel;

import java.util.ArrayList;
import java.util.List;

public class Model implements MinispecElement {

	private String packageName;
	List<Entity> entities;
	List<Interface> interfaces;
	List<Enumeration> enumerations;

	public Model() {
		this.entities = new ArrayList<>();
		this.interfaces = new ArrayList<>();
		this.enumerations = new ArrayList<>();
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void addInterface(Interface e) {
		this.interfaces.add(e);
	}

	public List<Interface> getInterfaces() {
		return interfaces;
	}

	public void addEnumeration(Enumeration enumeration) {
		this.enumerations.add(enumeration);
	}

	public List<Enumeration> getEnumeration() {
		return enumerations;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void accept(Visitor v) {
		v.visitModel(this);

		for (Interface inter : interfaces) {
			inter.accept(v);
		}

		for (Enumeration enumeration : enumerations) {
			enumeration.accept(v);
		}

		for (Entity entity : entities) {
			entity.accept(v);
		}

		v.visitPackage(this);

	}
}
