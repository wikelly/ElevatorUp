import java.util.ArrayList;
import java.util.Arrays;


public class PiecewiseLinearArrival {
	
	ArrayList<ArrayList<Double>> data;
	int numPeople;

	public PiecewiseLinearArrival(double p, double b, double a, int numPeople){
		data = new ArrayList<ArrayList<Double>>();
		this.numPeople = numPeople;
		loadArrivalRates(p,b,a);
		calculateCumulativeRates();
	}

	private void loadArrivalRates(double p, double b, double a){
				
		ArrayList<Double> rateVal = new ArrayList<Double>(Arrays.asList( 0.0, 0.0 ));
		ArrayList<Double> rateVal2 = new ArrayList<Double>(Arrays.asList( p-b, 0.0 ));
		ArrayList<Double> rateVal3 = new ArrayList<Double>(Arrays.asList( p, (2.0/(b+a))*100.0 ));
		ArrayList<Double> rateVal4 = new ArrayList<Double>(Arrays.asList(p+a,0.0));
		
		
		data.add(rateVal);
		data.add(rateVal2);
		data.add(rateVal3);
		data.add(rateVal4);
				
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
	
	public double nextTime(double nextRand, double sim_time){
		
		double ca = eventRateAtTime(sim_time);
		
		ca = ca - Math.log(nextRand);
		
		double next_time = inverseRate(ca);
		
		return next_time;

	}

}
