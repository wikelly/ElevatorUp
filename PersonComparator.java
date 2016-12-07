import java.util.Comparator;

public class PersonComparator implements Comparator<Person> {
	@Override
	public int compare(Person e1, Person e2) {
	    return e1.getFloor() < e2.getFloor() ? -1 : 1;
	}
}
