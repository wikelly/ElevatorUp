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
	    return type.getType() + " at time " + time;
	}
}