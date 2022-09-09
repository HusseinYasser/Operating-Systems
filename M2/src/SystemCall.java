import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SystemCall {
	
	File disk;
	
	String memory[];
	
	SystemCall()
	{
		disk = new File("Disk.txt");
		
		memory = new String[40];
		for(int i = 0  ; i < 40 ; ++i)
			memory[i] = null;	
	}
	
	
	
	
	public String read(int i)
	{
		return memory[i];
	}
	
	
	
	public void delete(int i)
	{
		memory[i] = null;
	}
	
	
	
	public void writeOnDisk(Queue<String> oldProcess) throws IOException
	{
		PrintWriter writer = new PrintWriter(disk);
		writer. print("");
		writer. close();
		
		//add the new data to the disk
		FileWriter myWriter = new FileWriter(disk);
		for(String x : oldProcess)
		{
			myWriter.write(x);
			myWriter.write("\n");
		}
		myWriter.close();
	}
	
	
	
	
	public void loadToMemory(int start,int end, Queue<String> file , boolean flag)
	{
		if(flag == false) {
			for(int i = start + 5 ; i < start + 8 ; ++i)
			{
				memory[i] = file.poll();
			}
		}
		for(int i = start + 8 ; i < end ; ++i)
			memory[i] = file.poll();
		
	}
	
	
	
	
	public void write(int i , String str)
	{
		memory[i] = str;
	}
	
	
	
	
	public Queue<String> readFromDisk() throws IOException
	{
		
        Queue<String> res = new LinkedList<String>();	
		BufferedReader br = new BufferedReader(new FileReader(disk));
		String st;
		while((st = br.readLine()) != null) {
			res.add(st);
		}
		return res;
	}
	
	public String input() {
		System.out.println("Enter your Input : ");
		Scanner m = new Scanner(System.in);
		String res = m.nextLine();
		return res;
	}
	
	public String readFile(String fileName) throws IOException
	{
		String res = "";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String st;
		while((st = br.readLine()) != null) {
			res += st +"\n";
		}
		return res;
	}
	
	public void writeFile(String fileName, String x) throws IOException
	{
		FileWriter myWriter = new FileWriter(fileName);
		myWriter.write(x);
		myWriter.close();
	}
	
	public void printMemory() {
		System.out.println("Memory Content");
		for(int i = 0 ; i < 40 ; ++i)
			System.out.println(read(i));
	}
	
	public void printDisk() throws IOException
	{
		System.out.println("Disk Content");
		BufferedReader br = new BufferedReader(new FileReader(disk));
		String st;
		while((st = br.readLine()) != null) {
			System.out.println(st);
		}
	}

}
