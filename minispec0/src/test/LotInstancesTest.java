package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import m2tiila.exemple5.Balise;
import m2tiila.exemple5.Repository;
import m2tiila.exemple5.Satellite;

class LotInstancesTest {

	Satellite satellite;
	Balise balise;

	@Test
	void testSerialisation() {
		// Création d'une instance de satellite
		satellite = new Satellite();
		satellite.setNom("Patrick");
		satellite.setId(1);

		// Création d'une instance de balise
		balise = new Balise();
		balise.setId(2);
		balise.setNom("Robert");
		balise.setPleine(false);

		// Sérialisation des instances créées
		Repository repo = new Repository();
		repo.addInstances(satellite);
		repo.addInstances(balise);
		repo.writeFile(new File("SauvegardeInstances.xml"));

		// Vérification du contenu du fichier de sauvegarde des instances
		String repositoryContent;
		try {
			repositoryContent = Files.readString(Path.of("SauvegardeInstances.xml"));
			assertTrue(repositoryContent.contains("<Instances>"));
			assertTrue(repositoryContent.contains("<Satellite nom=\"Patrick\" id=\"1\" />"));
			assertTrue(repositoryContent.contains("<Balise nom=\"Robert\" id=\"2\" pleine=\"false\" />"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testMaterialisation() {
		// Matérialisation des instances sauvegardées
		Repository repo = new Repository();
		repo.readFile(new File("SauvegardeInstances.xml"));

		Satellite satFromFile = (Satellite) repo.getInstances().get(0);
		assert (satFromFile.getId() == 1);
		assert (satFromFile.getNom().equals("Patrick"));

		Balise baliseFromFile = (Balise) repo.getInstances().get(1);
		assert (baliseFromFile.getId() == 2);
		assert (baliseFromFile.getNom().equals("Robert"));
		assert (!baliseFromFile.getPleine());

		// TODO: assert
	}

}
