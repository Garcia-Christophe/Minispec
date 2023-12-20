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
	String modelContent;
	String initValueContent;

	public PrettyPrinter() {
		initContents();
	}
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
	public void visitPackage(Model e) {
		String pascalizedName = pascalize(e.getPackageName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".mSpec");
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));

			modelContent = "model " + pascalizedName + ":";
			modelContent += entitiesContent;
			modelContent += "end_model;";

			writer.write(modelContent);
			writer.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initContents();
	}

	@Override
	public void visitEntity(Entity e) {
		if (e.getParentClassName() != null) {
			extendContent = " subType of (" + pascalize(e.getParentClassName()) + ")";
		}

		entitiesContent += "\n";
		entitiesContent += "\tentity " + pascalize(e.getName()) + extendContent + ":\n";
		entitiesContent += attributesContent;
		entitiesContent += "\tend_entity;\n";
		attributesContent = "";
	}

	@Override
	public void visitAttribute(Attribute e) {
		e.getType().accept(this);

		if (e.getInitialValue() != null) {
			initValueContent = " := " + e.getInitialValue();
		}

		attributesContent += "\t\t" + e.getName() + ": " +  typeContent + initValueContent + "\n";
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

	private void initContents() {
		entitiesContent = attributesContent = typeContent = modelContent = initValueContent = "";
	}

	private String pascalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
