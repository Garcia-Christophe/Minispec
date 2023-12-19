package root;

import java.util.List;
import java.util.ArrayList;

public class Balise extends MobilElement {

	private Satellite[] satellitesArray;

	private List<Satellite[]> satellitesArrayList;

	public Balise() {
		this.satellitesArray = new Satellite[20];
		this.satellitesArrayList = new ArrayList<>();
	}

	public void setSatellitesArray(Satellite[] satellitesArray) {
		this.satellitesArray = satellitesArray;
	}

	public Satellite[] getSatellitesArray() {
		return this.satellitesArray;
	}

	public boolean isSatellitesArrayListValid() {
		return this.satellitesArrayList.size() >= 0 && this.satellitesArrayList.size() <= 2147483647;
	}

	public void setSatellitesArrayList(List<Satellite[]> satellitesArrayList) {
		this.satellitesArrayList = satellitesArrayList;
	}

	public List<Satellite[]> getSatellitesArrayList() {
		return this.satellitesArrayList;
	}

}
