import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SIM {
	
	ArrayList<ArrayList<Double>> data;
	String rateFilename;
	double periodicTime;
	double periodicRate;
	TraceHandler random;
	double sim_time;
	double last_period_time;

	public SIM(String rateFile, String traceFile) throws IOException {
		data = new ArrayList<ArrayList<Double>>();
		rateFilename = rateFile;
		random = new TraceHandler(traceFile);
		
		loadArrivalRates();
		calculateCumulativeRates();
		sim_time = 0.0;
		last_period_time = 0.0;
	}

	private void loadArrivalRates() throws IOException{
		FileReader interarrivalReader = new FileReader(rateFilename);
		BufferedReader traceReader = new BufferedReader(interarrivalReader);
		
		String nextLine = "";
		nextLine = traceReader.readLine();
		while(nextLine != null){
			ArrayList<Double> rateVal = new ArrayList<Double>();
			String[] split = nextLine.split("\\s+");
			rateVal.add(Double.parseDouble(split[0]));
			rateVal.add(Double.parseDouble(split[1]));
			data.add(rateVal);
			
			nextLine = traceReader.readLine();
		}
		
		traceReader.close();
		
		periodicTime = data.get(data.size()-1).get(0);
	}
	
	private void calculateCumulativeRates(){
		data.get(0).add(0.0);
		for (int i = 1; i < data.size(); i++){
			double last_cumulative = data.get(i-1).get(2);
			
			double last_lambda = data.get(i-1).get(1);
			double lambda = data.get(i).get(1);
			
			double last_time = data.get(i-1).get(0);
			double time = data.get(i).get(0);
			
			double cumulative_j =  last_cumulative + 0.5*(lambda + last_lambda)*(time-last_time);
			data.get(i).add(cumulative_j);
		}
		
		periodicRate = data.get(data.size()-1).get(2);
	}
	
	private double get_s_j(int index){
		double rate_j = data.get(index).get(2);
		double lambda_j = data.get(index).get(1);
		double time_j = data.get(index).get(0);
		
		double lambdaDiff = data.get(index+1).get(1) - lambda_j;
		double timeDiff = data.get(index+1).get(0) - time_j;
		return lambdaDiff / timeDiff;
		
		
	}
		
	private double eventRateAtTime(double t){
		//Reduce t to periodic time
		t = t % periodicTime;
		
		int index = 0;
		
		while(data.get(index+1).get(0) < t) index++;
		
		double rate_j = data.get(index).get(2);
		double lambda_j = data.get(index).get(1);
		double time_j = data.get(index).get(0);
		
		double s_j = get_s_j(index);
		
		return rate_j + lambda_j*(t - time_j) + 0.5*s_j*Math.pow((t - time_j),2.0);
	}
	
	private double inverseRate(double y){
		int index = 0;
		
		while(data.get(index+1).get(2) < y) index++;
		
		double rate_j = data.get(index).get(2);
		double lambda_j = data.get(index).get(1);
		double time_j = data.get(index).get(0);
		
		double s_j = get_s_j(index);
		
		if(s_j == 0.0){
			return time_j + (y-rate_j)/lambda_j;
		}else{
			double numer = (2.0*(y-rate_j));
			double denom = (lambda_j + Math.sqrt( Math.pow(lambda_j, 2.0) + 2.0*s_j*(y-rate_j)));
			return time_j + numer/denom;
		}
		
	}
	
	public void run() throws IOException{
		double nextRand = random.getNextVal();
		
		while(nextRand != -1){
			double ca = eventRateAtTime(sim_time);
			
			ca = ca - Math.log(nextRand);
			
			if(ca >= periodicRate) {
				ca -= periodicRate;
				last_period_time += periodicTime;
			}
			
			double next_time = inverseRate(ca) + last_period_time;
			double interarrival = next_time - sim_time;
			System.out.format("OUTPUT %.6f %.6f \n", interarrival, next_time);
			sim_time = next_time;
			
			nextRand = random.getNextVal();
		}
		
		random.closeTrace();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		SIM sim  = new SIM(args[0], args[1]);
		
		sim.run();
	}

}
