package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import XMLIO.XMLAnalyserImports;
import generateurcode.GenerateurDeCode;
import metaModel.Model;
import metaModel.Primitive;

class GenerateurDeCodeTest {

	@Test
	void testCreationPackage() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple1.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de l'existence du package
		assertTrue(Files.exists(Paths.get("src/m2tiila/exemple1")));
	}

	@Test
	void testVersionMinimale() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple2.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification des classes générées
		assertTrue(Files.exists(Paths.get("src/m2tiila/exemple2/Balise.java")));
		assertTrue(Files.exists(Paths.get("src/m2tiila/exemple2/Satellite.java")));
	}

	@Test
	void testAssociationsSimples() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple3.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de la présence d'attributs (avec getter/setter)
		try {
			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple3/Balise.java"));
			assertTrue(baliseContent.contains("private String nom"));
			assertTrue(baliseContent.contains("public void setNom(String nom)"));
			assertTrue(baliseContent.contains("public String getNom()"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple3/Satellite.java"));
			assertTrue(satelliteContent.contains("private Balise balise"));
			assertTrue(satelliteContent.contains("public void setBalise(Balise balise)"));
			assertTrue(satelliteContent.contains("public Balise getBalise()"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testAssociationsMultiples() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple4.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de la présence d'attributs complexes
		// (avec getter/setter et isValid)
		try {
			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple4/Balise.java"));
			assertTrue(baliseContent.contains("private ArrayList<Satellite[]> satellitesArrayList"));
			assertTrue(baliseContent.contains("public boolean isSatellitesArrayListValid()"));
			assertTrue(baliseContent
					.contains("public void setSatellitesArrayList(ArrayList<Satellite[]> satellitesArrayList)"));
			assertTrue(baliseContent.contains("public ArrayList<Satellite[]> getSatellitesArrayList()"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple4/Satellite.java"));
			assertTrue(satelliteContent.contains("private HashSet<Balise> beaconsSet"));
			assertTrue(satelliteContent.contains("public boolean isBeaconsSetValid()"));
			assertTrue(satelliteContent.contains("public void setBeaconsSet(HashSet<Balise> beaconsSet)"));
			assertTrue(satelliteContent.contains("public HashSet<Balise> getBeaconsSet()"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testHeritage() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple5.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de l'héritage et de la résolution des dépendances
		try {
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple5/MobilElement.java")));

			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple5/Balise.java"));
			assertTrue(baliseContent.contains("public class Balise extends MobilElement"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple5/Satellite.java"));
			assertTrue(satelliteContent.contains("public class Satellite extends MobilElement"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testHeritage_DependanceCirculaire() {
		Exception e = Assertions.assertThrows(RuntimeException.class, () -> {
			XMLAnalyser analyser = new XMLAnalyser();
			List<Model> models = analyser.getModelsFromFilenamed("Exemple5-dependanceCirculaire.xml");
			GenerateurDeCode generateur = new GenerateurDeCode();

			// Vérification de la gestion de la dépendance circulaire
			for (Model model : models) {
				model.accept(generateur);
			}
		});
		assertEquals("Héritage : dépendance circulaire de classe", e.getMessage());
	}

	@Test
	void testHeritage_DefinitionMultiple() {
		Exception e = Assertions.assertThrows(RuntimeException.class, () -> {
			XMLAnalyser analyser = new XMLAnalyser();
			List<Model> models = analyser.getModelsFromFilenamed("Exemple5-definitionMultiple.xml");
			GenerateurDeCode generateur = new GenerateurDeCode();

			// Vérification de la gestion de la définition multiple d'attribut
			for (Model model : models) {
				model.accept(generateur);
			}
		});
		assertEquals("Attribut : définition multiple de l'attribut \"id\"", e.getMessage());
	}

	@Test
	void testImports() {
		XMLAnalyserImports analyserImports = new XMLAnalyserImports();
		Map<String, Primitive> primitives = analyserImports.getModelFromFilenamed("ExempleImports.xml");

		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple5.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		generateur.setPrimitives(primitives);
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification des imports
		try {
			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple5/Balise.java"));
			assertTrue(baliseContent.contains("import java.util.ArrayList"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple5/Satellite.java"));
			assertTrue(satelliteContent.contains("import java.util.ArrayList"));
			assertTrue(satelliteContent.contains("import java.util.HashSet"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testValeursInitiales() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple6.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de l'initialisation des valeurs
		try {
			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple6/Balise.java"));
			assertTrue(baliseContent.contains("this.pleine = false"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple6/Satellite.java"));
			assertTrue(satelliteContent.contains("this.idMaintainers = new int[] {2, 5, 6, 8, 10}"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testPresenceValueOf() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple6.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de la présence de la méthode valueOf()
		try {
			String baliseContent = Files.readString(Path.of("src/m2tiila/exemple6/Balise.java"));
			assertTrue(baliseContent.contains("public static Balise valueOf(Object obj)"));

			String satelliteContent = Files.readString(Path.of("src/m2tiila/exemple6/Satellite.java"));
			assertTrue(satelliteContent.contains("public static Satellite valueOf(Object obj)"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testPresenceRepository() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple6.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		// Vérification de la présence du fichier permettant la sérialisation et la
		// matérialisation des instances
		try {
			String repositoryContent = Files.readString(Path.of("src/m2tiila/exemple6/Repository.java"));
			assertTrue(repositoryContent.contains("List<Object> instances"));
			// readFile
			assertTrue(repositoryContent.contains("public void readFile(File f)"));
			assertTrue(repositoryContent.contains("if (elem.getTagName().equals(\"Balise\"))"));
			assertTrue(repositoryContent.contains("if (elem.getTagName().equals(\"Satellite\"))"));
			// WriteFile
			assertTrue(repositoryContent.contains("public void writeFile(File f)"));
			assertTrue(repositoryContent.contains("if (obj instanceof Balise)"));
			assertTrue(repositoryContent.contains("if (obj instanceof Satellite)"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testRebouclage() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple7.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		for (Model model : models) {
			model.accept(generateur);
		}

		try {
			// Vérification de la présence de tous les fichiers du méta-modèle
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/ArrayType.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Attribute.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/CollectionType.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/CollectionTypeEnum.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Entity.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Enumeration.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Interface.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/MiniSpec.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Model.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/NamedType.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Primitive.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Repository.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/TypeDesc.java")));
			assertTrue(Files.exists(Paths.get("src/m2tiila/exemple7/Visitor.java")));

			// Vérification de l'INTERFACE
			String interfaceContent = Files.readString(Path.of("src/m2tiila/exemple7/MiniSpec.java"));
			assertTrue(interfaceContent.contains("public interface MiniSpec"));

			// Vérification de l'ÉNUMERATION
			String enumerationContent = Files.readString(Path.of("src/m2tiila/exemple7/CollectionTypeEnum.java"));
			assertTrue(enumerationContent.contains("public enum CollectionTypeEnum"));
			assertTrue(enumerationContent.contains("LIST, SET, BAG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
