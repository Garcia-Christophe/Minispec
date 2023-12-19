package generateurcode;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import metaModel.Model;

class GenerateurDeCodeTest {

	@Test
	void test() {
		XMLAnalyser analyser = new XMLAnalyser();
		Model model = analyser.getModelFromFilenamed("Exemple4.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		model.accept(generateur);

		File directory = generateur.result();
		assertTrue(directory.exists());

		File[] files = directory.listFiles();
		assertTrue(files.length == 2);
		for (File file : files) {
			assertTrue(file.getName().equals("Balise.java") || file.getName().equals("Satellite.java"));
		}
	}

}
