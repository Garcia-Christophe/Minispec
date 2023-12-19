package generateurcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import metaModel.ArrayType;
import metaModel.Attribute;
import metaModel.CollectionType;
import metaModel.CollectionTypeEnum;
import metaModel.Entity;
import metaModel.Model;
import metaModel.NamedType;
import metaModel.Visitor;

public class GenerateurDeCode extends Visitor {

	File packageDir;
	String importsContent;
	String attributesContent;
	String constructorContent;
	String methodsContent;

	String attributeType;

	public GenerateurDeCode() {
		initContents();
	}

	public File result() {
		return packageDir;
	}

	@Override
	public void visitModel(Model e) {
		packageDir = new File("src/root");
		if (!packageDir.exists())
			packageDir.mkdir();
	}

	@Override
	public void visitEntity(Entity e) {
		String pascalizedName = pascalize(e.getName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".java");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));

			String constructor = "\tpublic " + pascalizedName + "() {\n" + constructorContent + "\t}\n\n";

			String classContent = "package root;\n\n";
			classContent += importsContent + "\n";
			classContent += "public class " + pascalizedName + " {\n\n";
			classContent += attributesContent;
			classContent += constructor;
			classContent += methodsContent;
			classContent += "}\n";

			writer.write(classContent);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		initContents();
	}

	@Override
	public void visitAttribute(Attribute e) {
		String pascalizedName = pascalize(e.getName());

		// Attributs
		attributesContent += "\tprivate " + attributeType + " " + e.getName() + ";\n\n";

		// Initialisation
		if (e.getType() instanceof CollectionType) {
			constructorContent += "\t\tthis." + e.getName() + " = new ";
			if (attributeType.startsWith(pascalize(CollectionTypeEnum.SET.toString().toLowerCase()))) {
				// HashSet
				constructorContent += "HashSet<>();\n";
				if (!importsContent.contains("import java.util.HashSet")) {
					// Import
					importsContent += "import java.util.HashSet;\n";
				}
			} else {
				// ArrayList
				constructorContent += "ArrayList<>();\n";
				if (!importsContent.contains("import java.util.ArrayList")) {
					// Import
					importsContent += "import java.util.ArrayList;\n";
				}
			}

			// isValid method
			methodsContent += "\tpublic boolean is" + pascalizedName + "Valid() {\n";
			methodsContent += "\t\treturn this." + e.getName() + ".size() >= " + ((CollectionType) e.getType()).getMin()
					+ " && this." + e.getName() + ".size() <= " + ((CollectionType) e.getType()).getMax() + ";\n";
			methodsContent += "\t}\n\n";
		} else if (e.getType() instanceof ArrayType) {
			ArrayType arrayType = ((ArrayType) e.getType());
			constructorContent += "\t\tthis." + e.getName() + " = new "
					+ attributeType.substring(0, attributeType.length() - 2) + "[" + arrayType.getSize() + "];\n";
		}

		// Setter
		methodsContent += "\tpublic void set" + pascalizedName + "(" + attributeType + " " + e.getName() + ") {\n";
		methodsContent += "\t\tthis." + e.getName() + " = " + e.getName() + ";\n";
		methodsContent += "\t}\n\n";
		// Getter
		methodsContent += "\tpublic " + attributeType + " get" + pascalizedName + "() {\n";
		methodsContent += "\t\treturn this." + e.getName() + ";\n";
		methodsContent += "\t}\n\n";

		// Vider
		attributeType = "";
	}

	@Override
	public void visitNamedType(NamedType e) {
		attributeType = e.getName();
	}

	@Override
	public void visitCollectionType(CollectionType e) {
		if (e.getCollectionTypeEnum() == CollectionTypeEnum.LIST
				|| e.getCollectionTypeEnum() == CollectionTypeEnum.BAG) {
			// List
			if (!importsContent.contains("import java.util.List")) {
				// Import
				importsContent += "import java.util.List;\n";
			}

			// Type de l'attribut
			attributeType = "List<" + attributeType + ">";
		} else if (e.getCollectionTypeEnum() == CollectionTypeEnum.SET) {
			// Set
			if (!importsContent.contains("import java.util.Set")) {
				// Import
				importsContent += "import java.util.Set;\n";
			}

			// Type de l'attribut
			attributeType = "Set<" + attributeType + ">";
		}
	}

	@Override
	public void visitArrayType(ArrayType e) {
		// Type de l'attribut
		attributeType = attributeType + "[]";
	}

	private void initContents() {
		importsContent = attributesContent = constructorContent = methodsContent = "";
	}

	private String pascalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
