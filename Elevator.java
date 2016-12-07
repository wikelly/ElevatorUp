//Elevator Class
// Number of people
// List of floors to go to

import java.util.PriorityQueue;

public class Elevator {
	int currentFloor, numStops, floorsTraveled;
	
	PriorityQueue<Person> personQueue;
	
	public Elevator() {
		currentFloor = 0;
		numStops = 0;
		floorsTraveled = 0;
		
		personQueue = new PriorityQueue<Person>(2, new PersonComparator());
	}

	public void addPerson(Person thisGuy) {
		personQueue.add(thisGuy);
	}
}
