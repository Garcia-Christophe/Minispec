package generateurcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import metaModel.ArrayType;
import metaModel.Attribute;
import metaModel.CollectionType;
import metaModel.CollectionTypeEnum;
import metaModel.Entity;
import metaModel.Model;
import metaModel.NamedType;
import metaModel.Primitive;
import metaModel.Visitor;

public class GenerateurDeCode extends Visitor {

	File packageDir;
	String importsContent;
	String attributesContent;
	String constructorContent;
	String methodsContent;

	String attributeType;
	String packageName;

	Map<String, Primitive> primitives;

	public GenerateurDeCode() {
		initContents();
		this.primitives = new HashMap<>();
	}

	public File result() {
		return packageDir;
	}

	@Override
	public void visitModel(Model e) {
		packageName = e.getPackageName().replace(".", "/").toLowerCase();
		packageDir = new File("src/" + packageName);
		if (!packageDir.exists())
			packageDir.mkdirs();
	}

	@Override
	public void visitEntity(Entity e) {
		String pascalizedName = pascalize(e.getName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".java");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));

			String constructor = "\tpublic " + pascalizedName + "() {\n" + constructorContent + "\t}\n\n";

			String extendContent = "";
			if (e.getParentClassName() != null) {
				extendContent = " extends " + e.getParentClassName();
			}

			String classContent = "package " + packageName.replace("/", ".") + ";\n\n";
			classContent += importsContent + "\n";
			classContent += "public class " + pascalizedName + extendContent + " {\n\n";
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
			if (e.getInitialValue() == null) {
				constructorContent += "\t\tthis." + e.getName() + " = new " + attributeType + "();\n";
			}

			// isValid method
			methodsContent += "\tpublic boolean is" + pascalizedName + "Valid() {\n";
			methodsContent += "\t\treturn this." + e.getName() + ".size() >= " + ((CollectionType) e.getType()).getMin()
					+ " && this." + e.getName() + ".size() <= " + ((CollectionType) e.getType()).getMax() + ";\n";
			methodsContent += "\t}\n\n";
		} else if (e.getType() instanceof ArrayType && e.getInitialValue() == null) {
			ArrayType arrayType = ((ArrayType) e.getType());
			constructorContent += "\t\tthis." + e.getName() + " = new "
					+ attributeType.substring(0, attributeType.length() - 2) + "[" + arrayType.getSize() + "];\n";
		} else if (e.getInitialValue() != null) {
			constructorContent += "\t\tthis." + e.getName() + " = " + e.getInitialValue() + ";\n";
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
			String type = "ArrayList"; // Valeur par défaut
			String importPath = "java.util.ArrayList"; // Valeur par défaut
			if (this.primitives.get("List") != null) {
				type = this.primitives.get("List").getType(); // Importé des primitives
				importPath = this.primitives.get("List").getPackageName(); // Importé des primitives
			}
			if (!importsContent.contains("import " + importPath)) {
				// Import
				importsContent += "import " + importPath + ";\n";
			}

			// Type de l'attribut
			attributeType = type + "<" + attributeType + ">";
		} else if (e.getCollectionTypeEnum() == CollectionTypeEnum.SET) {
			// Set
			String type = "HashSet"; // Valeur par défaut
			String importPath = "java.util.HashSet"; // Valeur par défaut
			if (this.primitives.get("Set") != null) {
				type = this.primitives.get("Set").getType(); // Importé des primitives
				importPath = this.primitives.get("Set").getPackageName(); // Importé des primitives
			}
			if (!importsContent.contains("import " + importPath)) {
				// Import
				importsContent += "import " + importPath + ";\n";
			}

			// Type de l'attribut
			attributeType = type + "<" + attributeType + ">";
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

	public void setPrimitives(Map<String, Primitive> primitives) {
		this.primitives = primitives;
	}

}
