package m2tiil.monpackage;

import java.util.ArrayList;

public class Balise extends MobilElement {

	private Satellite[] satellitesArray;

	private ArrayList<Satellite[]> satellitesArrayList;

	public Balise() {
		this.satellitesArray = new Satellite[20];
		this.satellitesArrayList = new ArrayList<Satellite[]>();
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

	public void setSatellitesArrayList(ArrayList<Satellite[]> satellitesArrayList) {
		this.satellitesArrayList = satellitesArrayList;
	}

	public ArrayList<Satellite[]> getSatellitesArrayList() {
		return this.satellitesArrayList;
	}

}
