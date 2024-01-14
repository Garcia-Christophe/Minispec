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
		List<Model> models = analyser.getModelsFromFilenamed("Exemple6.xml");
		GenerateurDeCode generateur = new GenerateurDeCode();
		generateur.setPrimitives(primitives);
		for (Model model : models) {
			model.accept(generateur);
		}

		/*
		 * Satellite sat = new Satellite(); sat.setNom("patrick"); sat.setId(1);
		 * 
		 * Balise balise = new Balise(); balise.setId(1); balise.setNom("robert");
		 * balise.setPleine(false);
		 * 
		 * Repository repo = new Repository(); repo.addInstances(sat);
		 * repo.addInstances(balise); repo.writeFile(new File("repo.xml"));
		 * 
		 * repo.readFile(new File("repo.xml")); Satellite satFromFile = (Satellite)
		 * repo.getInstances().get(0); assert (satFromFile.getNom().equals("patrick"));
		 * assert (satFromFile.getId() == 1);
		 * 
		 * Balise baliseFromFile = (Balise) repo.getInstances().get(1); assert
		 * (baliseFromFile.getId() == 1); assert
		 * (baliseFromFile.getNom().equals("robert")); assert
		 * (!baliseFromFile.getPleine());
		 */
	}

}
