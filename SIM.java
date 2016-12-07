import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class SIM {
	double sim_time;
	int numPeople = 100;
	TraceHandler randomTrace;
	
	
	int floors;
	int elevators;
	double g;
	double b;
	double a;
	String traceFilename;
	int days;
	
	ArrayList<Elevator> elevatorList;
	PriorityQueue<Event> eventQueue;
	
	

	public SIM(int floors, int elevators, double g, double b, double a,
			String traceFilename, int days) throws IOException {
		super();
		this.floors = floors;
		this.elevators = elevators;
		this.g = g;
		this.b = b;
		this.a = a;
		this.traceFilename = traceFilename;
		this.days = days;
		
		randomTrace = new TraceHandler(traceFilename);
		elevatorList = new ArrayList<Elevator>();
		eventQueue = new PriorityQueue<Event>(2,new EventComparator());
	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		int floors = Integer.parseInt(args[0]);
		int elevators = Integer.parseInt(args[1]);
		double g = Double.parseDouble(args[2]);
		double b = Double.parseDouble(args[3]);
		double a = Double.parseDouble(args[4]);
		String traceFilename = args[5];
		int days = Integer.parseInt(args[6]);
		
		SIM sim;
		
		try {
			 sim = new SIM(floors, elevators, g, b, a, traceFilename, days);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
