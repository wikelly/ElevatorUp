import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TraceHandler {
	String traceFilename;
	BufferedReader traceReader;
	
	public TraceHandler(String filename) throws IOException {
		super();
		this.traceFilename = filename;
		loadTrace();
	}
	
	public void loadTrace() throws FileNotFoundException{
		FileReader interarrivalReader = new FileReader(traceFilename);
		traceReader = new BufferedReader(interarrivalReader);
	}
	
	public double getNextVal() throws IOException{
		String next_val = "";
		next_val = traceReader.readLine();
		if(next_val == null){
			return -1;
		}
		return Double.parseDouble(next_val);
	}
	
	public void closeTrace() throws IOException{
		traceReader.close();
	}
}
