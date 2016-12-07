public class Person {
	private double arrival;
	private int floor;

	public Person(int floor, double arrival) {
		this.floor = floor;
		this.arrival = arrival;
	}

	public int getFloor() {
		return floor;
	}

	public double getArrival() {
		return arrival;
	}
}