package osp;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Process {
	
	CodeParser parser;
	static int globalID = 1;
	int id;
	Queue<String> instructions;
	HashMap<String,Object> variables;
	Object tmp;
	
	
	public Process(File file, CodeParser parser) throws IOException
	{
		id = globalID++;
		this.parser = parser;
		instructions = parser.parse(file);
		variables = new HashMap<String,Object>();
	}
	
	
	public boolean semWait(Resource r) // --> Why it returns boolean ? to indicate wether to block in general queue or not
    {                                  // --> if return true that means resource was busy and i blocked the process so i have to block it also in OS class
	                                   // --> if returned false means that the resource was free and has been taken by this process when called semWait
		
		if( r.m.val == true) //can not be taken so i should block this process
		{
			r.blocked.add(this);
			return true;
		}
		else
		{
			r.m.val = true;
			r.m.ownerID =this.id;
			return false;
		}
	}
	
	
	public Process semSignal(Resource r)
	{
		
		if(r.m.ownerID == this.id)
		{
			
			if(r.blocked.size() == 0) //noMore blocked processes over this resources
			{
				
				r.m.val = false;
				r.m.ownerID = -1;
				return null;
			}
			
			else
			{
				
				r.m.val = true;
				Process freed = r.blocked.poll(); //This Process should be removed from the general blocked queue and shall be added to general ready queue
				r.m.ownerID = freed.id;
				//System.out.println(freed.id);
				return freed;
			}
			
		}
		return null;
		
	}

}
