package XMLIO;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import metaModel.Model;

import java.util.List;

class XMLAnalyserTest {

	@Test
	void test1() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple1.xml");
		assertTrue(models != null);
		assertTrue(models.get(0).getEntities().size() == 0);
	}

	@Test
	void test2() {
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromFilenamed("Exemple2.xml");
		assertTrue(models != null);
		assertTrue(models.get(0).getEntities().size() == 2);
	}

	@Test
	void test3() {
		String src = "<Root model=\"3\"> <Model id=\"3\" /> </Root>";
		XMLAnalyser analyser = new XMLAnalyser();
		List<Model> models = analyser.getModelsFromString(src);
		assertTrue(models != null);
		assertTrue(models.get(0).getEntities().size() == 0);
	}

}