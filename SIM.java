import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
		
		randomTrace = new TraceHandler(traceFilename);
		elevatorQueue = new ArrayDeque<Elevator>();
		arrivals = new ArrayList<PiecewiseLinearArrival>();
		eventQueue = new PriorityQueue<Event>(2,new EventComparator());
		lobbyQueue = new ArrayDeque<Person>();
		
		
		initElevators();
		//initSim();
	}
	
	private void initSim() throws IOException{
		p = p * (a+b);
		initArrivals();
		sim_time = 0;
		for (int i = 0; i < floors; i++){
			double nextArrival = arrivals.get(i).nextTime(randomTrace.getNextVal(), sim_time);
			Person newPerson = new Person(i+1, nextArrival);
			eventQueue.add(new Event(EventType.PERSON, nextArrival, newPerson));
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
					double nextArrival = arrivals.get(nextFloor-1).nextTime(randomTrace.getNextVal(), sim_time);
					Person newPerson = new Person(nextFloor, nextArrival);
					eventQueue.add(new Event(EventType.PERSON, nextArrival, newPerson));
					
					if(!elevatorQueue.isEmpty()){
						Elevator elevator = elevatorQueue.remove();
						elevator.addPerson(nextPerson);
						eventQueue.add(new Event(EventType.ARRIVAL, sim_time + elevator.goToNextFloor() + elevator.getDepartTime(1),elevator));
					}else{
						lobbyQueue.add(nextEvent.getPerson());
					}
					break;
				case ARRIVAL:
					if(nextElevator.currentFloor == 0){
						if(lobbyQueue.isEmpty()){
							elevatorQueue.add(nextElevator);
						}else{
							while(!lobbyQueue.isEmpty() && (nextElevator.personQueue.size() < 20)){
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
				printState();
			}
		}
	}
	
	public void printState(){
		System.out.println(sim_time);
		System.out.println(eventQueue);
		System.out.println(elevatorQueue);
		System.out.println(lobbyQueue);
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
		
		sim.run();

	}

}
