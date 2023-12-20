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

import metaModel.Primitive;

public class XMLAnalyserImports {

	public Map<String, Primitive> getPrimitivesFromDocument(Document document) {
		Map<String, Primitive> primitives = new HashMap<>();
		Element el = document.getDocumentElement();
		NodeList nodes = el.getElementsByTagName("Primitive");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n instanceof Element && n.getNodeName().equals("Primitive")) {
				Element e = (Element) n;
				Primitive primitive = new Primitive();
				primitive.setName(e.getAttribute("name"));
				primitive.setType(e.getAttribute("type"));
				primitive.setPackageName(e.getAttribute("package"));

				primitives.put(primitive.getName(), primitive);
			}
		}

		return primitives;
	}

	public Map<String, Primitive> getPrimitivesFromInputStream(InputStream stream) {
		try {
			// création d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			Document document = constructeur.parse(stream);
			return getPrimitivesFromDocument(document);

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

	public Map<String, Primitive> getPrimitivesFromString(String contents) {
		InputStream stream = new ByteArrayInputStream(contents.getBytes());
		return getPrimitivesFromInputStream(stream);
	}

	public Map<String, Primitive> getPrimitivesFromFile(File file) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return getPrimitivesFromInputStream(stream);
	}

	public Map<String, Primitive> getModelFromFilenamed(String filename) {
		File file = new File(filename);
		return getPrimitivesFromFile(file);
	}

}
