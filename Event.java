import java.util.Comparator;

public class Event {
	private EventType type;
	private float time;
	private Elevator elevator;
	private Person person;

	Event(EventType type, float time, Person person) {
		this.type = type;
		this.time = time;
		this.person = person;
		this.elevator = null;
	}

	Event(EventType type, float time, Elevator elevator) {
		this.type = type;
		this.time = time;
		this.elevator = elevator;
		this.person = null;
	}

	EventType getType() {
		return type;
	}

	float getTime() {
		return time;
	}

	@Override
	public String toString() {
	    return type.getType() + " at time " + time;
	}
}