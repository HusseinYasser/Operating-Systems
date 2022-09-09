package osp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CodeParser {
	
	public CodeParser()
	{
		
	}
	
	public Queue<String> parse(File file) throws IOException
	{
		Queue<String> res = new LinkedList<String>();;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		String str1="";
		while((st = br.readLine()) != null) {
			String arr[] = st.split(" ");
			if(arr[0].equals("assign"))
			{
				if(arr[2].equals("input"))
				{
					res.add(arr[2]);
					res.add(arr[0]+" "+arr[1]);
				}
				else
				{
					res.add(arr[2]+" "+arr[3]);
					res.add(arr[0]+" "+arr[1]);
				}
			}
			else
			{
				res.add(st);
			}
			
		}
		return res;
		
	}

}
