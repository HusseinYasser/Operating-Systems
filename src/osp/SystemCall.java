package osp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SystemCall {
	
	
	public SystemCall()
	{
		
	}
	
	public void assign(Process p , String variableName)
	{
		p.variables.put(variableName, p.tmp);
		p.tmp = null;
	}
	
	
	public Object input(Process p)
	{
		System.out.println("Please Enter your Input: ");
		Scanner m = new Scanner(System.in);
		Object ret = (Object)(m.nextLine());
		return p.tmp = ret;
	}
	
	
	public void printFromTo(Object a,Object b)
	{
		int start = Integer.parseInt((String)a) , end = Integer.parseInt((String)b);
		for(int i = start;i<=end;++i)
		{
			System.out.print(i+" ");
		}
		System.out.println();
	}
	
	
	public void writeFile(String fileName , String data, HashMap<String,File> files) throws IOException 
	{
		if(files.containsKey(fileName) == false)
		{
			System.out.println("File is not found and a new file with this name is created");
			
			File ff = new File(fileName);
			files.put(fileName, ff);
			
		}
		FileWriter myWriter = new FileWriter(files.get(fileName));
		myWriter.write(data);
		myWriter.close();
	}
	
	
	public String readFile(String fileName , HashMap<String,File> files,Process p) throws IOException
	{
		
		String res = "";
		
		if(files.containsKey(fileName) == false)
			return "File is Not Found\n";
		
		BufferedReader br = new BufferedReader(new FileReader(files.get(fileName)));
		
		String str = br.readLine();
		res = str;
		
		while((str = br.readLine())!= null)
		{
			res+= "\n";
			res += str;
		}
		
		p.tmp = res;
		
		return res;
		
	}
	
	
	public void print(Object data)
	{
		System.out.println(data.toString());
	}
	
	
	

}
