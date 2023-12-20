model M2tiil.monpackage:
	entity MobilElementnull:
		nom: String
		id: Integer
	end_entity;

	entity Balise subType of (MobilElement):
		satellitesArray: Array[20] of Satellite
		satellitesArrayList: List[0 : 2147483647] of Array[20] of Satellite
	end_entity;

	entity Satellite subType of (MobilElement):
		beaconsList: List[0 : 10] of Balise
		beaconsSet: Set[2 : 10] of Balise
		beaconsBag: Bag[0 : 2147483647] of Balise
	end_entity;
end_model;