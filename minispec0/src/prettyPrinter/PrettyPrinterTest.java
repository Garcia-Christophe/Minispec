package prettyPrinter;

import org.junit.jupiter.api.Test;

import XMLIO.XMLAnalyser;
import metaModel.Model;

class PrettyPrinterTest {

	@Test
	void test() {
		XMLAnalyser analyser = new XMLAnalyser();
		Model model = analyser.getModelFromFilenamed("minispec0/Exemple5.xml");
		PrettyPrinter pp = new PrettyPrinter();
		model.accept(pp);
		System.out.println(pp.result());
	}

}
