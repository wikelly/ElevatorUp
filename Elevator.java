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

		if(Math.abs(floor - currentFloor) > 1.0)
			moveTime = 16.0 + 5.0 * ((double)Math.abs(floor-currentFloor) - 2.0);
		else
			moveTime = 8.0;

		floorsTraveled += Math.abs(floor - currentFloor);
		currentFloor = floor;
		numStops++;

		return moveTime;
	}

	@Override
	public String toString() {
		return "Elevator [currentFloor=" + currentFloor + ", numStops="
				+ numStops + ", floorsTraveled=" + floorsTraveled
				+ ", personQueue=" + personQueue + ", waitTimes=" + totalWait()
				+ "]";
	}
	
	public double totalWait(){
		double sum = 0.0;
		for (double x : waitTimes) sum += x;
		return sum;
	}

	public int getStops() {
		return numStops;
	}

	public int getFloors() {
		return floorsTraveled;
	}

	public ArrayList<Double> getWaitTimes() {
		return waitTimes;

	}

	public double dropOff(double arriveTime) {
		int numberPeople = 0;
		ArrayList<Person> floorPeople = new ArrayList<Person>();

		while(!personQueue.isEmpty() && (personQueue.peek().getFloor() == currentFloor)) {
			numberPeople++;
			floorPeople.add(personQueue.remove());
		}

		double departTime = getDepartTime(numberPeople);
		double minTransit = minTransitTime(floorPeople.get(0).getFloor());

		for(int i = 0; i < floorPeople.size(); i++) {
			double waitTime = arriveTime + departTime - floorPeople.get(i).getArrival() - minTransit;
			if(waitTime < -.001){
				System.out.println(waitTime);
			}
			waitTimes.add(waitTime);
		}

		return departTime;
	}

	private double minTransitTime(int floor) {
		double moveTime = 0; 

		if(floor > 1)
			moveTime = 16.0 + 5.0 * ((double)floor - 2.0);
		else
			moveTime = 8.0;

		return moveTime + 6.0;
	}

	public double getDepartTime(int numPeople) {
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
