package prettyPrinter;

import java.util.List;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import metaModel.Model;

class PrettyPrinterTest {

	@Test
	void test() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple7.xml");
		PrettyPrinter pp = new PrettyPrinter();
		for (Model model : models) {
			model.accept(pp);
		}
	}

}
