import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
	@Override
	public int compare(Event e1, Event e2) {
	    if (e1.getTime() == e2.getTime()) {
	        if (e1.getType() == e2.getType()) {
	            return 0;
	        }
	        return 0;               
	    }
	    return e1.getTime() < e2.getTime() ? -1 : 1;
	}
}