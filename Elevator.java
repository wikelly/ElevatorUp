//Elevator Class
// Number of people
// List of floors to go to

//Events
// Elevator arrival on base floors
// Elevator arrival on other floors
// People arrival
import java.util.TreeMap;

public class Elevator {
	SortedMap<int,int> floorPeople;
	int currentFloor, numStops;
	
	public Elevator() {
		floorPeople = new TreeMap();
		currentFloor = 0;
		numStops = 0;
	}

	public void addPerson(int floor) {
		if(floorPeople.containsKey(floor))
			floorPeople.get(floor) += 1;
		else
			floorPeople.put(floor, 1);
	}
}
