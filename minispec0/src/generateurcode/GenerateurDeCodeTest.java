package generateurcode;

import java.util.Map;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import XMLIO.XMLAnalyserImports;
import metaModel.Model;
import metaModel.Primitive;

class GenerateurDeCodeTest {

	@Test
	void test() {
		XMLAnalyserImports analyserImports = new XMLAnalyserImports();
		Map<String, Primitive> primitives = analyserImports.getModelFromFilenamed("ExempleImports.xml");

		XMLAnalyser analyser = new XMLAnalyser();
		Model model = analyser.getModelFromFilenamed("Exemple5.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		generateur.setPrimitives(primitives);
		model.accept(generateur);
	}

}
