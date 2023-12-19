package metaModel;

import java.util.ArrayList;
import java.util.List;

public class Model implements MinispecElement {

	private String packageName;
	List<Entity> entities;

	public Model() {
		this.entities = new ArrayList<>();
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void accept(Visitor v) {
		v.visitModel(this);

		for (Entity entity : entities) {
			entity.accept(v);
		}
	}

}
