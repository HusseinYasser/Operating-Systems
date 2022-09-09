import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Parser {
	
	public Parser()
	{
		
	}
	
	
	public Queue<String> parse(File file) throws IOException
	{
		Queue<String> res = new LinkedList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String st;
		
		
		while((st = br.readLine()) != null) {
			
			String arr[] = st.split(" ");
			
			res.add(st);
			
		}
		return res;
		
	}

}
