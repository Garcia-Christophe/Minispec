package root;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Satellite {

	private String nom;

	private Integer id;

	private List<Balise> beaconsList;

	private Set<Balise> beaconsSet;

	private List<Balise> beaconsBag;

	public Satellite() {
		this.beaconsList = new ArrayList<>();
		this.beaconsSet = new HashSet<>();
		this.beaconsBag = new ArrayList<>();
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public boolean isBeaconsListValid() {
		return this.beaconsList.size() >= 0 && this.beaconsList.size() <= 10;
	}

	public void setBeaconsList(List<Balise> beaconsList) {
		this.beaconsList = beaconsList;
	}

	public List<Balise> getBeaconsList() {
		return this.beaconsList;
	}

	public boolean isBeaconsSetValid() {
		return this.beaconsSet.size() >= 2 && this.beaconsSet.size() <= 10;
	}

	public void setBeaconsSet(Set<Balise> beaconsSet) {
		this.beaconsSet = beaconsSet;
	}

	public Set<Balise> getBeaconsSet() {
		return this.beaconsSet;
	}

	public boolean isBeaconsBagValid() {
		return this.beaconsBag.size() >= 0 && this.beaconsBag.size() <= 2147483647;
	}

	public void setBeaconsBag(List<Balise> beaconsBag) {
		this.beaconsBag = beaconsBag;
	}

	public List<Balise> getBeaconsBag() {
		return this.beaconsBag;
	}

}
