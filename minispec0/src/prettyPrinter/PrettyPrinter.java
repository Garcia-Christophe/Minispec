package prettyPrinter;

import metaModel.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrettyPrinter extends Visitor {
	File packageDir;
	String result;
	String entitiesContent;
	String extendContent;
	String attributesContent;
	String typeContent;

	public String result() {
		return result;
	}

	@Override
	public void visitModel(Model e) {
		packageDir = new File("src/spec");
		if (!packageDir.exists())
			packageDir.mkdir();
	}

	@Override
	public void visitEntity(Entity e) {
		String pascalizedName = pascalize(e.getName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".mSpec");

		if (e.getParentClassName() != null) {
			extendContent = " subType of (" + pascalize(e.getParentClassName()) + ")";
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));
			entitiesContent = "";
			entitiesContent += "entity " + pascalize(e.getName()) + extendContent + ":\n";
			entitiesContent += attributesContent;
			entitiesContent += "end_entity;";
			attributesContent = "";

			writer.write(entitiesContent);
			writer.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void visitAttribute(Attribute e) {
		e.getType().accept(this);

		attributesContent += "\t" + e.getName() + ": " +  typeContent + "\n";
	}

	@Override
	public void visitNamedType(NamedType e) {
		typeContent = e.getName();
	}

	@Override
	public void visitCollectionType(CollectionType e) {
		e.getElementType().accept(this);

		typeContent = pascalize(e.getCollectionTypeEnum().toString().toLowerCase() )+ "[" + e.getMin() + " : " + e.getMax() + "] of " + typeContent;
	}

	@Override
	public void visitArrayType(ArrayType e) {
		e.getElementType().accept(this);

		typeContent = "Array[" + e.getSize() + "] of " + typeContent;
	}

	private String pascalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
