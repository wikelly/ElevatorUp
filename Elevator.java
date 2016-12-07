//Elevator Class
// Number of people
// List of floors to go to

import java.util.PriorityQueue;
import java.util.ArrayList;

public class Elevator {
	int currentFloor, numStops, floorsTraveled;
	
	PriorityQueue<Person> personQueue;
	ArrayList<Double> waitTimes;
	
	public Elevator() {
		currentFloor = 0;
		numStops = 0;
		floorsTraveled = 0;
		
		personQueue = new PriorityQueue<Person>(2, new PersonComparator());
		waitTimes = new ArrayList<Double>();
	}

	public void addPerson(Person thisGuy) {
		personQueue.add(thisGuy);
	}

	public double goToNextFloor() {
		int floor; 
		double moveTime = 0;

		if(personQueue.isEmpty())
			floor = 0;
		else
			floor = personQueue.peek().getFloor();

		if(floor - currentFloor > 1)
			moveTime = 16.0 + 5.0 * ((floor-currentFloor) - 2);
		else
			moveTime = 8.0;

		currentFloor = floor;
		floorsTraveled += Math.abs(floor - currentFloor);
		numStops++;

		return moveTime;
	}

	public double dropOff(double arriveTime) {
		int numberPeople = 0;
		ArrayList<Person> floorPeople = new ArrayList<Person>();

		while(personQueue.peek().getFloor() == currentFloor) {
			numberPeople++;
			floorPeople.add(personQueue.remove());
		}

		double departTime = getDepartTime(numberPeople);
		double minTransit = minTransitTime(floorPeople.get(0).getFloor());

		for(int i = 0; i < floorPeople.size(); i++) {
			waitTimes.add(arriveTime + departTime - floorPeople.get(i).getArrival() - minTransit);
		}

		return departTime;
	}

	private double minTransitTime(int floor) {
		double moveTime = 0; 

		if(floor - 0 > 1)
			moveTime = 16.0 + 5.0 * ((floor-0) - 2);
		else
			moveTime = 8.0;

		return moveTime + 6;
	}

	private double getDepartTime(int numPeople) {
		if(numPeople == 1)
			return 3.0;
		else if(numPeople == 2)
			return 5.0;
		else if(numPeople == 3)
			return 7.0;
		else if(numPeople == 4)
			return 9.0;
		else if(numPeople == 5)
			return 11.0;
		else if(numPeople == 6)
			return 13.0;
		else if(numPeople == 7)
			return 15.0;
		else if(numPeople == 8)
			return 17.0;
		else if(numPeople == 9)
			return 19.0;
		else if(numPeople == 10)
			return 22.0;
		return -1.0;
	}
}
