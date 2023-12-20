package m2tilal.monpackage;

import java.util.ArrayList;

public class Balise extends MobilElement {

	private boolean pleine;

	private Satellite[] satellitesArray;

	private ArrayList<Satellite[]> satellitesArrayList;

	public Balise() {
		this.pleine = false;
		this.satellitesArray = new Satellite[20];
		this.satellitesArrayList = new ArrayList<Satellite[]>();
	}

	public void setPleine(boolean pleine) {
		this.pleine = pleine;
	}

	public boolean getPleine() {
		return this.pleine;
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
