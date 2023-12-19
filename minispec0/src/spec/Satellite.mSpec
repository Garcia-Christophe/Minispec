entity Satellite subType of (MobilElement):
	beaconsList: List[0 : 10] of Balise
	beaconsSet: Set[2 : 10] of Balise
	beaconsBag: Bag[0 : 2147483647] of Balise
end_entity;