model M2tILAl.monpackage:
	entity MobilElementnull:
		nom: String
		id: Integer
	end_entity;

	entity Balise subType of (MobilElement):
		pleine: boolean := false
		satellitesArray: Array[20] of Satellite := false
		satellitesArrayList: List[0 : 2147483647] of Array[20] of Satellite := false
	end_entity;

	entity Satellite subType of (MobilElement):
		beaconsList: List[0 : 10] of Balise := false
		beaconsSet: Set[2 : 10] of Balise := false
		beaconsBag: Bag[0 : 2147483647] of Balise := false
		idMaintainers: Array[5] of int := new int[] {2, 5, 6, 8, 10}
	end_entity;
end_model;