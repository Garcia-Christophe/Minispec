package prettyPrinter;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import metaModel.Model;

import java.util.List;

class PrettyPrinterTest {

	@Test
	void test() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("minispec0/Exemple5.xml");
		PrettyPrinter pp = new PrettyPrinter();
		for (Model model: models){
			model.accept(pp);
		}
	}

}
