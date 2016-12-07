import java.util.Comparator;

public class Event {
	private EventType type;
	private double time;
	private Elevator elevator;
	private Person person;

	Event(EventType type, double time, Person person) {
		this.type = type;
		this.time = time;
		this.person = person;
		this.elevator = null;
	}

	Event(EventType type, double time, Elevator elevator) {
		this.type = type;
		this.time = time;
		this.elevator = elevator;
		this.person = null;
	}

	EventType getType() {
		return type;
	}

	double getTime() {
		return time;
	}

	@Override
	public String toString() {
		String personString = "";
		String elevatorString = "";
		if (elevator != null) elevatorString = " " + String.valueOf(elevator.currentFloor);
	    return type.getType() + elevatorString + " at time " + time;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public Person getPerson() {
		return person;
	}
}