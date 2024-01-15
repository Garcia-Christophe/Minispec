package prettyPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import metaModel.ArrayType;
import metaModel.Attribute;
import metaModel.CollectionType;
import metaModel.Entity;
import metaModel.Enumeration;
import metaModel.Interface;
import metaModel.Model;
import metaModel.NamedType;
import metaModel.Visitor;

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
		try {
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
		} else if (e.getParentInterfaceName() != null) {
			extendContent = " implements (" + pascalize(e.getParentInterfaceName()) + ")";
		} else {
			extendContent = "";
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

		attributesContent += "\t\t" + e.getName() + ": " + typeContent + initValueContent + "\n";
	}

	@Override
	public void visitNamedType(NamedType e) {
		typeContent = e.getName();
	}

	@Override
	public void visitCollectionType(CollectionType e) {
		e.getElementType().accept(this);

		typeContent = pascalize(e.getCollectionTypeEnum().toString().toLowerCase()) + "[" + e.getMin() + " : "
				+ e.getMax() + "] of " + typeContent;
	}

	@Override
	public void visitArrayType(ArrayType e) {
		e.getElementType().accept(this);

		typeContent = "Array[" + e.getSize() + "] of " + typeContent;
	}

	@Override
	public void visitInterface(Interface e) {
		entitiesContent += "\n";
		entitiesContent += "\tinterface " + pascalize(e.getName()) + ":\n";
		entitiesContent += "\tend_interface;\n";
	}

	@Override
	public void visitEnumeration(Enumeration e) {
		entitiesContent += "\n";
		entitiesContent += "\tenumeration " + pascalize(e.getName()) + ":\n";
		entitiesContent += "\t\titems: " + e.getItems() + "\n";
		entitiesContent += "\tend_enumeration;\n";
	}

	private void initContents() {
		entitiesContent = attributesContent = typeContent = modelContent = initValueContent = "";
	}

	private String pascalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
