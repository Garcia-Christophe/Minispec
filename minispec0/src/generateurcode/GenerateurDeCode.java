package generateurcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import metaModel.ArrayType;
import metaModel.Attribute;
import metaModel.CollectionType;
import metaModel.CollectionTypeEnum;
import metaModel.Entity;
import metaModel.Interface;
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

	String repositoryWriterContent;
	String repositoryReaderContent;

	Map<String, Primitive> primitives;

	public GenerateurDeCode() {
		initContents();
		this.primitives = new HashMap<>();
		this.repositoryWriterContent = this.repositoryReaderContent = "";
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
		generateRepository();
	}

	@Override
	public void visitEntity(Entity e) {
		String pascalizedName = pascalize(e.getName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".java");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));

			String constructor = "\tpublic " + pascalizedName + "() {\n" + constructorContent + "\t}\n\n";

			String extendsContent = "";
			if (e.getParentClassName() != null) {
				extendsContent = "extends " + e.getParentClassName() + " ";
			}

			String implementsContent = "";
			if (e.getParentInterfaceName() != null) {
				implementsContent = "implements " + e.getParentInterfaceName() + " ";
			}

			// isValid method
			methodsContent += "\tpublic static " + pascalizedName + " valueOf(Object obj) {\n";
			methodsContent += "\t\ttry {\n";
			methodsContent += "\t\t\treturn (" + pascalizedName + ") obj;\n";
			methodsContent += "\t\t} catch (Exception e) {\n";
			methodsContent += "\t\t\treturn null;\n";
			methodsContent += "\t\t}\n";
			methodsContent += "\t}\n\n";

			String classContent = "package " + packageName.replace("/", ".") + ";\n\n";
			classContent += importsContent + "\n";
			classContent += "public class " + pascalizedName + " " + extendsContent + implementsContent + "{\n\n";
			classContent += attributesContent;
			classContent += constructor;
			classContent += methodsContent;
			classContent += "}\n";

			writer.write(classContent);
			writer.close();

			// nouvelle entity ajoutée & writer et au reader du repository
			repositoryWriterContent += "\t\t\t\tif (obj instanceof " + e.getName() + ") {\n" + "\t\t\t\t\t"
					+ e.getName() + " newInstance = (" + e.getName() + ") obj;\n" + "\t\t\t\t\tinstanceString += \"\\t<"
					+ e.getName();

			repositoryReaderContent += "\t\t\t\t\tif (elem.getTagName().equals(\"" + e.getName() + "\")) {\n"
					+ "\t\t\t\t\t\t" + e.getName() + " newInstance = new " + e.getName() + "();\n";

			for (Attribute attr : e.getAttributes()) {
				if (attr.getType() instanceof NamedType) {
					attr.getType().accept(this);
					// ajout des attributs de l'entity au writer et au reader de repository
					repositoryWriterContent += " " + attr.getName() + "=\\\"\" + newInstance.get"
							+ pascalize(attr.getName()) + "() + \"\\\"";

					repositoryReaderContent += "\t\t\t\t\t\tnewInstance.set" + pascalize(attr.getName()) + "("
							+ pascalize(attributeType) + ".valueOf(elem.getAttribute(\"" + attr.getName() + "\")));\n";
				}
			}

			// fin de l'entity au writer et au reader de repository
			repositoryWriterContent += "/>\\n\";\n\t\t\t\t}\n";
			repositoryReaderContent += "\t\t\t\t\t\tthis.addInstances(newInstance);\n" + "\t\t\t\t\t}\n";
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

	@Override
	public void visitPackage(Model e) {
		try {
			String fileContent = Files.readString(Path.of(packageDir.getPath() + "/Repository.java"));

			fileContent = fileContent.replace("//writers", repositoryWriterContent);

			fileContent = fileContent.replace("//readers", repositoryReaderContent);

			BufferedWriter bw = new BufferedWriter(new FileWriter(packageDir.getPath() + "/Repository.java"));
			bw.write(fileContent);
			bw.close();
		} catch (Exception exception) {
			System.err.println(exception);
		}
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

	public void generateRepository() {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(packageDir.getPath() + "/Repository.java"));
			String fileContent = "package " + this.packageName.replace("/", ".") + ";\n" + "\n"
					+ "import org.w3c.dom.Document;\n" + "import org.w3c.dom.Element;\n" + "import org.w3c.dom.Node;\n"
					+ "import org.w3c.dom.NodeList;\n" + "\n" + "import javax.xml.parsers.DocumentBuilder;\n"
					+ "import javax.xml.parsers.DocumentBuilderFactory;\n" + "import java.io.*;\n"
					+ "import java.util.ArrayList;\n" + "import java.util.List;\n" + "\n"
					+ "public class Repository {\n" + "    List<Object> instances;\n" + "\n"
					+ "    public Repository() {\n" + "        instances = new ArrayList<>();\n" + "    }\n" + "\n"
					+ "    public void readFile(File f) {\n" + "        try {\n"
					+ "            // création d'une fabrique de documents\n"
					+ "            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();\n" + "\n"
					+ "            // création d'un constructeur de documents\n"
					+ "            DocumentBuilder constructeur = fabrique.newDocumentBuilder();\n"
					+ "            Document document = constructeur.parse(new FileInputStream(f));\n"
					+ "            Element firstElement = document.getDocumentElement();\n"
					+ "            NodeList nodeList = firstElement.getChildNodes();\n"
					+ "            for (int i = 0; i < nodeList.getLength(); i++) {\n"
					+ "                Node node = nodeList.item(i);\n"
					+ "                if (node instanceof Element) {\n"
					+ "                    Element elem = (Element) node;\n" + "//readers\n" + "                }\n"
					+ "            }\n" + "        } catch (Exception e) {\n" + "            System.err.println(e);\n"
					+ "        }\n" + "    }\n" + "\n" + "    public void writeFile(File f) {\n" + "        try {\n"
					+ "            BufferedWriter bw = new BufferedWriter(new FileWriter(f.getPath(), false));\n"
					+ "            String instanceString = \"<Instance>\\n\";\n"
					+ "            for (Object obj : instances) {\n" + "//writers\n" + "            }\n"
					+ "            instanceString += \"</Instance>\";\n" + "            bw.write(instanceString);\n"
					+ "            bw.close();\n" + "        } catch (Exception e) {\n"
					+ "            System.err.println(e);\n" + "        }\n" + "    }\n" + "\n"
					+ "    public void addInstances(Object instance) {\n" + "        this.instances.add(instance);\n"
					+ "    }\n" + "\n" + "    public List<Object> getInstances() {\n"
					+ "        return this.instances;\n" + "    }\n" + "}\n";
			br.write(fileContent);
			br.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Override
	public void visitInterface(Interface e) {
		String pascalizedName = pascalize(e.getName());
		File javaFile = new File(packageDir.getPath() + "/" + pascalizedName + ".java");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile.getPath(), false));

			String classContent = "package " + packageName.replace("/", ".") + ";\n\n";
			classContent += "public interface " + pascalizedName + "{\n\n";
			classContent += "}\n";

			writer.write(classContent);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
