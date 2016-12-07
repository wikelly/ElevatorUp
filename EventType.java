public enum EventType {
	ARRIVAL("Arrive on Base Floor"), 
	LEAVE("Leaving Base Floor"), 
	PERSON("New Person Arrival");

	private final String type;
	EventType(String t) {
		type = t;
	}

	String getType() {
		return type;
	}
}
