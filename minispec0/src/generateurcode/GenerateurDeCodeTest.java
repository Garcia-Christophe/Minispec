package generateurcode;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import metaModel.Model;

class GenerateurDeCodeTest {

	@Test
	void test() {
		XMLAnalyser analyser = new XMLAnalyser();
		Model model = analyser.getModelFromFilenamed("Exemple5.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		model.accept(generateur);
	}

}
