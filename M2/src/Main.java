import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
	
	public static void main(String [] args) throws IOException
	{
		
		Scanner m = new Scanner(System.in);
		
		File program1 = new File("C:\\Users\\admin\\Desktop\\OSPrograms\\Program_1.txt");
		
		File program2 = new File("C:\\Users\\admin\\Desktop\\OSPrograms\\Program_2.txt");
		
		File program3 = new File("C:\\Users\\admin\\Desktop\\OSPrograms\\Program_3.txt");
		
		System.out.println("Enter The Number of Ticks for the Scheduler : ");
		
		int ticks = m.nextInt();
		
		/*Since we only have 3 processes so store the arrival time of each of the programs in 
		   an array of length 3
		*/
		
		int [] arrivalTime = new int[3];
		
		
		System.out.println("Enter the Arrival Time of Program 1 : ");
		
		arrivalTime[0] = m.nextInt();
		
		System.out.println("Enter the Arrival Time of Program 2 : ");
		
		arrivalTime[1] = m.nextInt();
		
		System.out.println("Enter the Arrival Time of Program 3 : ");
		
		arrivalTime[2] = m.nextInt();
		
		System.out.println("-------------------------------------------------");
		OS os = new OS(ticks, arrivalTime);
		while(os.finished > 0)
			os.run();
		System.out.println("All is Finished");
	}

}
