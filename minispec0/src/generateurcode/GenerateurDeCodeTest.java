package generateurcode;

import java.util.List;
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
		List<Model> models = analyser.getModelsFromFilenamed("Exemple5.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		generateur.setPrimitives(primitives);
		for (Model model: models){
			model.accept(generateur);
		}
	}

}
