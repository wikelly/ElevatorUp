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
	int numPedestrians;
	
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
		numPedestrians = 0;
		
		randomTrace = new TraceHandler(traceFilename);
		elevatorQueue = new ArrayDeque<Elevator>();
		arrivals = new ArrayList<PiecewiseLinearArrival>();
		eventQueue = new PriorityQueue<Event>(2,new EventComparator());
		lobbyQueue = new ArrayDeque<Person>();
		
		p = p * (this.a+this.b);
		initElevators();
		initArrivals();
		//initSim();
	}
	
	private void initSim() throws IOException{
		
		
		sim_time = 0;
		for (int i = 0; i < floors; i++){
			arrivals.get(i).reset();
			double nextArrival = arrivals.get(i).nextTime(randomTrace.getNextVal(), sim_time);
			Person newPerson = new Person(i+1, nextArrival);
			eventQueue.add(new Event(EventType.PERSON, nextArrival, newPerson));
			numPedestrians++;
		}
	}
	
	private void initArrivals(){
		arrivals.clear();
		for (int i = 0; i < floors; i++){
			arrivals.add(new PiecewiseLinearArrival(b + i*p + 60.0, b, a, numPeople));
		}
	}
	
	private void initElevators(){
		for (int i = 0; i < elevators; i++){
			elevatorQueue.add(new Elevator());
		}
	}
	
	public void run() throws IOException{
		for(int i = 0; i < days; i++){
			initSim();
			
			while(!eventQueue.isEmpty()){
				
				Event nextEvent = eventQueue.remove();
				sim_time = nextEvent.getTime();
				Elevator nextElevator = nextEvent.getElevator();
				Person nextPerson = nextEvent.getPerson();
				
				switch(nextEvent.getType()){
				case PERSON :
					int nextFloor = nextPerson.getFloor();
					if(!arrivals.get(nextFloor-1).ended){
						double nextArrival = arrivals.get(nextFloor-1).nextTime(randomTrace.getNextVal(), sim_time);
					
						if(nextArrival != -1) {
							Person newPerson = new Person(nextFloor, nextArrival);
							eventQueue.add(new Event(EventType.PERSON, nextArrival, newPerson));
							numPedestrians++;
						}
					}
					if(!elevatorQueue.isEmpty()){
						Elevator elevator = elevatorQueue.remove();
						elevator.addPerson(nextPerson);
						eventQueue.add(new Event(EventType.ARRIVAL, sim_time + elevator.goToNextFloor() + elevator.getDepartTime(1),elevator));
					}else{
						lobbyQueue.add(nextEvent.getPerson());
						if(lobbyQueue.size() > maxQueueLength) maxQueueLength = lobbyQueue.size();
					}
					break;
				case ARRIVAL:
					if(nextElevator.currentFloor == 0){
						if(lobbyQueue.isEmpty()){
							elevatorQueue.add(nextElevator);
						}else{
							while(!lobbyQueue.isEmpty() && (nextElevator.personQueue.size() < 10)){
								nextElevator.addPerson(lobbyQueue.remove());
							}
							eventQueue.add(new Event(EventType.ARRIVAL, sim_time + nextElevator.goToNextFloor() + nextElevator.getDepartTime(nextElevator.personQueue.size()),nextElevator));
						}

					}else{
						eventQueue.add(new Event(EventType.LEAVE, sim_time + nextElevator.dropOff(sim_time),nextElevator));
					}
					break;
				case LEAVE: 
					eventQueue.add(new Event(EventType.ARRIVAL, sim_time + nextElevator.goToNextFloor(),nextElevator));
					break;
				}
				//printState();
			}
		}
	}
	
	public void printState(){
		System.out.println(sim_time);
		System.out.println(eventQueue);
		System.out.println(elevatorQueue);
		System.out.println(lobbyQueue);
	}

	public void computeStatistics() {
		System.out.println("Printing out statistics");

		int floornum = 1;
		for (PiecewiseLinearArrival x : arrivals){
			System.out.println("floor " + floornum + " total arrivals " + x.arrivals + " in triangular rate distribution"
					+ "(" + x.data.get(1).get(0) + "," + x.data.get(2).get(0) + "," + x.data.get(3).get(0) + ")");
			floornum++;
		}
		
		System.out.println("Number of Elevators: " + elevatorQueue.size());
		ArrayList<Elevator> allElevators = new ArrayList<Elevator>();

		while(!elevatorQueue.isEmpty()) {
			allElevators.add(elevatorQueue.remove());
		}
		
		System.out.println("Number of pedestrians: " + numPedestrians);

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
		int bin = 0;
		ArrayList<Double> histogram = new ArrayList<Double>();
		histogram.add(0.0);
		for(int i = 0; i < waitTimes.size(); i++) {
			if(waitTimes.get(i) < 60*(bin+1)){
				histogram.set(bin, histogram.get(bin) + 1.0);
			} else {
				bin++;
				histogram.add(1.0);
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
			System.out.format("%.5f ",histogram.get(i));
		}
		System.out.println("");
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		int floors = Integer.parseInt(args[0]);
		int elevators = Integer.parseInt(args[1]);
		double g = Double.parseDouble(args[2]);
		double b = Double.parseDouble(args[3]);
		double a = Double.parseDouble(args[4]);
		String traceFilename = args[5];
		int days = Integer.parseInt(args[6]);
		
		SIM sim = new SIM(floors, elevators, g, b, a, traceFilename, days);
		// SIM sim = new SIM(2,4,0.1,15,5,"uniform-0-1-00.dat",300);
		
		sim.run();
		sim.printState();
		sim.computeStatistics();
	}

}
