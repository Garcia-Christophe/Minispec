package m2tiil.monpackage;

import java.util.ArrayList;
import java.util.HashSet;

public class Satellite extends MobilElement {

	private ArrayList<Balise> beaconsList;

	private HashSet<Balise> beaconsSet;

	private ArrayList<Balise> beaconsBag;

	public Satellite() {
		this.beaconsList = new ArrayList<Balise>();
		this.beaconsSet = new HashSet<Balise>();
		this.beaconsBag = new ArrayList<Balise>();
	}

	public boolean isBeaconsListValid() {
		return this.beaconsList.size() >= 0 && this.beaconsList.size() <= 10;
	}

	public void setBeaconsList(ArrayList<Balise> beaconsList) {
		this.beaconsList = beaconsList;
	}

	public ArrayList<Balise> getBeaconsList() {
		return this.beaconsList;
	}

	public boolean isBeaconsSetValid() {
		return this.beaconsSet.size() >= 2 && this.beaconsSet.size() <= 10;
	}

	public void setBeaconsSet(HashSet<Balise> beaconsSet) {
		this.beaconsSet = beaconsSet;
	}

	public HashSet<Balise> getBeaconsSet() {
		return this.beaconsSet;
	}

	public boolean isBeaconsBagValid() {
		return this.beaconsBag.size() >= 0 && this.beaconsBag.size() <= 2147483647;
	}

	public void setBeaconsBag(ArrayList<Balise> beaconsBag) {
		this.beaconsBag = beaconsBag;
	}

	public ArrayList<Balise> getBeaconsBag() {
		return this.beaconsBag;
	}

}
