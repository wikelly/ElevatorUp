import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class SIM {
	double sim_time;
	int numPeople = 100;
	TraceHandler randomTrace;
	
	
	int floors;
	int elevators;
	double p;
	double b;
	double a;
	String traceFilename;
	int days;
	int maxQueueLength;
	
	Queue<Elevator> elevatorQueue;
	ArrayList<PiecewiseLinearArrival> arrivals;
	PriorityQueue<Event> eventQueue;
	Queue<Person> lobbyQueue;
	

	public SIM(int floors, int elevators, double g, double b, double a,
			String traceFilename, int days) throws IOException {
		super();
		this.floors = floors;
		this.elevators = elevators;
		this.p = g;
		this.b = b * 60.0;
		this.a = a * 60.0;
		this.traceFilename = traceFilename;
		this.days = days;
		maxQueueLength = 0;
		
		randomTrace = new TraceHandler(traceFilename);
		elevatorQueue = new ArrayDeque<Elevator>();
		arrivals = new ArrayList<PiecewiseLinearArrival>();
		eventQueue = new PriorityQueue<Event>(2,new EventComparator());
		lobbyQueue = new ArrayDeque<Person>();
		
		
		initElevators();
		initSim();
	}
	
	private void initSim() throws IOException{
		p = p * (a+b);
		initArrivals();
		sim_time = 0;
		for (int i = 0; i < floors; i++){
			double nextArrival = arrivals.get(i).nextTime(randomTrace.getNextVal(), sim_time);
			Person newPerson = new Person(i, nextArrival);
			eventQueue.add(new Event(EventType.ARRIVAL, nextArrival, newPerson));
		}
	}
	
	private void initArrivals(){
		for (int i = 0; i < floors; i++){
			arrivals.add(new PiecewiseLinearArrival(b + i*p + 1, b, a, numPeople));
		}
	}
	
	private void initElevators(){
		for (int i = 0; i < elevators; i++){
			elevatorQueue.add(new Elevator());
		}
	}
	
	public void run(){
		
	}
	
	public void printState(){
		System.out.println(eventQueue);
	}

	public void computeStatistics() {
		System.out.println("Printing out statistics");

		System.out.println("Number of Elevators: " + elevatorQueue.size());
		ArrayList<Elevator> allElevators = new ArrayList<Elevator>();

		while(!elevatorQueue.isEmpty()) {
			allElevators.add(elevatorQueue.remove());
		}

		// Begin calculating the number of stops per elevator per day
		double stops = 0.0;
		for(int i = 0; i < allElevators.size(); i++) {
			stops += allElevators.get(i).getStops();
		}
		stops = stops / (double)(elevators) / (double)(days);
		System.out.println("Number of stops per elevator per day: " + stops);
		System.out.println("OUTPUT " + stops);


		// Begin calculating number of traveled floors per elevator per day
		double floors = 0.0;
		for(int i = 0; i < allElevators.size(); i++) {
			floors += allElevators.get(i).getFloors();
		}
		floors = floors / (double)(elevators) / (double)(days);
		System.out.println("Number of floors traveled per elevator per day: " + floors);
		System.out.println("OUTPUT " + floors);


		// Print out the longest that the queue of people ever was
		System.out.println("Longest queue of people length: " + maxQueueLength);
		System.out.println("OUTPUT " + maxQueueLength);

		// Begin the calculation for the wait time histogram
		ArrayList<Double> waitTimes = new ArrayList<Double>();
		for(int i = 0; i < allElevators.size(); i++) {
			waitTimes.addAll(allElevators.get(i).getWaitTimes());
		}
		Collections.sort(waitTimes);
		Collections.reverse(waitTimes);
		int bin = 0;
		ArrayList<Double> histogram = new ArrayList<Double>();
		for(int i = 0; i < waitTimes.size(); i++) {
			if(waitTimes.get(i) < 60*(bin+1)){
				histogram.set(bin, histogram.get(i) + 1.0);
			} else {
				bin++;
				histogram.add(bin, 1.0);
			}
		}
		double sum = 0;
		for(int i = 0; i < histogram.size(); i++) {
			sum += histogram.get(i);
		}
		for(int i = 0; i < histogram.size(); i++) {
			histogram.set(i, histogram.get(i)/sum);
		}
		System.out.println("Printing out the histogram");
		System.out.print("OUTPUT ");
		for(int i = 0; i < histogram.size(); i++) {
			System.out.print(histogram.get(i) + " ");
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		/*int floors = Integer.parseInt(args[0]);
		int elevators = Integer.parseInt(args[1]);
		double g = Double.parseDouble(args[2]);
		double b = Double.parseDouble(args[3]);
		double a = Double.parseDouble(args[4]);
		String traceFilename = args[5];
		int days = Integer.parseInt(args[6]);*/
		
		// SIM sim = new SIM(floors, elevators, g, b, a, traceFilename, days);
		SIM sim = new SIM(2,4,0.1,15,5,"uniform-0-1-big.dat",300);
		
		
		sim.printState();

	}

}
