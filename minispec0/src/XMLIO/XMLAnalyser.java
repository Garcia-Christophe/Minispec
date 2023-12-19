package XMLIO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import metaModel.ArrayType;
import metaModel.Attribute;
import metaModel.CollectionType;
import metaModel.CollectionTypeEnum;
import metaModel.Entity;
import metaModel.MinispecElement;
import metaModel.Model;
import metaModel.NamedType;
import metaModel.TypeDesc;

public class XMLAnalyser {

	// Les clés des 2 Map sont les id

	// Map des elements XML
	protected Map<String, Element> xmlElementIndex;
	// Map des instances de la syntaxe abstraite (metamodel)
	protected Map<String, MinispecElement> minispecIndex;

	public XMLAnalyser() {
		this.xmlElementIndex = new HashMap<String, Element>();
		this.minispecIndex = new HashMap<String, MinispecElement>();
	}

	protected Model modelFromElement(Element e) {
		return new Model();
	}

	protected Entity entityFromElement(Element e) {
		String name = e.getAttribute("name");
		Entity entity = new Entity();
		entity.setName(name);

		// Ajoute l'entity au model
		Model model = (Model) minispecElementFromXmlElement(this.xmlElementIndex.get(e.getAttribute("model")));
		model.addEntity(entity);

		return entity;
	}

	protected Attribute attributeFromElement(Element e) {
		Attribute attr = new Attribute();
		String name = e.getAttribute("name");
		attr.setName(name);

		// Ajoute l'attribut à l'entity
		Entity entity = (Entity) minispecElementFromXmlElement(this.xmlElementIndex.get(e.getAttribute("entity")));
		entity.getAttributes().add(attr);

		return attr;
	}

	protected NamedType namedTypeFromElement(Element e) {
		NamedType namedType = new NamedType();
		String name = e.getAttribute("name");
		namedType.setName(name);

		if (e.getAttribute("attribute") != null && !e.getAttribute("attribute").isEmpty()) {
			// Ajoute le namedType à l'attribut
			Attribute attr = (Attribute) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("attribute")));
			attr.setType(namedType);
		} else if (e.getAttribute("elementTypeOf") != null && !e.getAttribute("elementTypeOf").isEmpty()) {
			TypeDesc parentType = (TypeDesc) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("elementTypeOf")));

			if (parentType instanceof CollectionType) {
				// Ajoute le namedType à la collectionType
				((CollectionType) parentType).setElementType(namedType);
			} else if (parentType instanceof ArrayType) {
				// Ajoute le namedType à la arrayType
				((ArrayType) parentType).setElementType(namedType);
			}
		}

		return namedType;
	}

	protected CollectionType collectionTypeFromElement(Element e) {
		CollectionType collectionType = new CollectionType();

		// type enum
		CollectionTypeEnum typeEnum = CollectionTypeEnum.valueOf(e.getAttribute("type"));
		collectionType.setCollectionTypeEnum(typeEnum);

		// minimum
		if (e.getAttribute("min") != null && !e.getAttribute("min").isEmpty()) {
			int min = Integer.valueOf(e.getAttribute("min"));
			collectionType.setMin(min);
		}

		// maximum
		if (e.getAttribute("max") != null && !e.getAttribute("max").isEmpty()) {
			int max = Integer.valueOf(e.getAttribute("max"));
			collectionType.setMax(max);
		}

		if (e.getAttribute("attribute") != null && !e.getAttribute("attribute").isEmpty()) {
			// Ajoute la collection à l'attribut
			Attribute attr = (Attribute) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("attribute")));
			attr.setType(collectionType);
		} else if (e.getAttribute("elementTypeOf") != null && !e.getAttribute("elementTypeOf").isEmpty()) {
			TypeDesc parentType = (TypeDesc) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("elementTypeOf")));

			if (parentType instanceof CollectionType) {
				// Ajoute le namedType à la collectionType
				((CollectionType) parentType).setElementType(collectionType);
			} else if (parentType instanceof ArrayType) {
				// Ajoute le namedType à la arrayType
				((ArrayType) parentType).setElementType(collectionType);
			}
		}

		return collectionType;
	}

	protected ArrayType arrayTypeFromElement(Element e) {
		ArrayType arrayType = new ArrayType();

		// size
		int size = Integer.valueOf(e.getAttribute("size"));
		arrayType.setSize(size);

		if (e.getAttribute("attribute") != null && !e.getAttribute("attribute").isEmpty()) {
			// Ajoute l'array à l'attribut
			Attribute attr = (Attribute) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("attribute")));
			attr.setType(arrayType);
		} else if (e.getAttribute("elementTypeOf") != null && !e.getAttribute("elementTypeOf").isEmpty()) {
			TypeDesc parentType = (TypeDesc) minispecElementFromXmlElement(
					this.xmlElementIndex.get(e.getAttribute("elementTypeOf")));

			if (parentType instanceof CollectionType) {
				// Ajoute le namedType à la collectionType
				((CollectionType) parentType).setElementType(arrayType);
			} else if (parentType instanceof ArrayType) {
				// Ajoute le namedType à la arrayType
				((ArrayType) parentType).setElementType(arrayType);
			}
		}

		return arrayType;
	}

	protected MinispecElement minispecElementFromXmlElement(Element e) {
		String id = e.getAttribute("id");
		MinispecElement result = this.minispecIndex.get(id);
		if (result != null)
			return result;

		String tag = e.getTagName();
		if (tag.equals("Model")) {
			result = modelFromElement(e);
		} else if (tag.equals("Entity")) {
			result = entityFromElement(e);
		} else if (tag.equals("Attribute")) {
			result = attributeFromElement(e);
		} else if (tag.equals("NamedType")) {
			result = namedTypeFromElement(e);
		} else if (tag.equals("CollectionType")) {
			result = collectionTypeFromElement(e);
		} else if (tag.equals("ArrayType")) {
			result = arrayTypeFromElement(e);
		}
		this.minispecIndex.put(id, result);

		return result;
	}

	// alimentation du map des elements XML
	protected void firstRound(Element el) {
		NodeList nodes = el.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element) {
				Element child = (Element) n;
				String id = child.getAttribute("id");
				this.xmlElementIndex.put(id, child);
			}
		}
	}

	// alimentation du map des instances de la syntaxe abstraite (metamodel)
	protected void secondRound(Element el) {
		NodeList nodes = el.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element) {
				minispecElementFromXmlElement((Element) n);
			}
		}
	}

	public Model getModelFromDocument(Document document) {
		Element e = document.getDocumentElement();

		firstRound(e);

		secondRound(e);

		Model model = (Model) this.minispecIndex.get(e.getAttribute("model"));

		return model;
	}

	public Model getModelFromInputStream(InputStream stream) {
		try {
			// création d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			Document document = constructeur.parse(stream);
			return getModelFromDocument(document);

		} catch (ParserConfigurationException pce) {
			System.out.println("Erreur de configuration du parseur DOM");
			System.out.println("lors de l'appel à fabrique.newDocumentBuilder();");
		} catch (SAXException se) {
			System.out.println("Erreur lors du parsing du document");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		} catch (IOException ioe) {
			System.out.println("Erreur d'entrée/sortie");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		}
		return null;
	}

	public Model getModelFromString(String contents) {
		InputStream stream = new ByteArrayInputStream(contents.getBytes());
		return getModelFromInputStream(stream);
	}

	public Model getModelFromFile(File file) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getModelFromInputStream(stream);
	}

	public Model getModelFromFilenamed(String filename) {
		File file = new File(filename);
		return getModelFromFile(file);
	}
}
