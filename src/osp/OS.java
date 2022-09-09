package osp;

import java.io.File;
import java.io.IOException;
import java.util.*;


class Pair
{
	Process p ;
	int time ;
	
	Pair(Process p , int time)
	{
		this.p  = p ;
		this.time = time;
	}
}

public class OS {
	
	//Scheduler scheduler;
	HashMap<String,Resource> resources;
	HashMap<String,File> files;
	Queue<Process>Ready;
	Queue<Process>Blocked;
	Queue<Pair> toBeSubmitted;
	
	
	SystemCall systemcall; //For Any Hardware instruction handling
	int ticks;
	
	public OS()
	{
		
		resources = new HashMap<String,Resource>();
		
		files = new HashMap<String,File>();
		
		systemcall = new SystemCall();
		
		Ready = new LinkedList<Process>();
		
		Blocked = new LinkedList<Process>();
		
		toBeSubmitted = new LinkedList<Pair>();
		
		//scheduler = new Scheduler();
	}
	
	//Execute method for the processor --> method that executes the number of ticks
	
	public void addResource(String name,Resource r)
	{
		resources.put(name, r);
	}
	
	public boolean admitProcess(int clockCycles)
	{
		boolean c = false;
		while(true)
		{
			
			Pair tmp = toBeSubmitted.peek();
			if(tmp == null)
				return c;
			if(tmp == null)
				return false;
			if(tmp.time == clockCycles)
			{
				this.Ready.add(tmp.p);
				toBeSubmitted.poll();
				c |= true;
			}
			else
				return c;
		}
		
	}
	
	public void UserInputProcess(Process p , int time)
	{
		
		Pair xyz = new Pair(p , time);
		
		toBeSubmitted.add(xyz);
		
	}
	
	public boolean execute(String instruction , Process x) throws IOException
	{
		
		boolean isFinished = false;
		boolean isBlocked = false;
		//check if the instruction is HW related or non HW related
		String arr[] = instruction.split(" ");
		
		//non HW instructions
		if(arr[0].equals("semWait") || arr[0].equals("semSignal"))
		{
			
			if(arr[0].equals("semWait"))
			{
				boolean check = x.semWait(resources.get(arr[1]));
				if(check == true) {
					this.Blocked.add(x);
					System.out.println("Process "+x.id+" has got Blocked over the "+arr[1]+" Resource!");
					isBlocked = true;
					//System.out.println("-------------------------------------------------");
				}
				else
					System.out.println("Process "+x.id+" has acquired the "+arr[1]+" Resource");
			}
			else if(arr[0].equals("semSignal")==true) // --> SemSignal calling method 
			{
				Process check = x.semSignal(resources.get(arr[1]));
				if(check == null)
				{
					System.out.println("Resource "+arr[1]+" is released by Process "+x.id +" it is free now");
				}
				else
				{
					System.out.println("Resource "+arr[1] + " is released by Process "+x.id+ " and it is acquired by the first one in it's blocked queue Process "
							+ check.id);
					//Removing from the original Blocked Queue
					this.Blocked.remove(check);
					//Adding to Original Ready Queue the Process that given control of the mutex in the semSignal Operation
					this.Ready.add(check);
				}
			}
			
		}
		
		//HW instructions
		else
		{
			//File
			if(arr[0].equals("readFile"))
			{
				String dataRead = systemcall.readFile(arr[1], files , x);
				//System.out.println(dataRead);
			}
			else if(arr[0].equals("writeFile"))
			{
				systemcall.writeFile(arr[1], (String)x.variables.get(arr[2]), files);
			}
			//outputTerminal
			else if(arr[0].equals("printFromTo"))
			{
				systemcall.printFromTo(x.variables.get(arr[1]), x.variables.get(arr[2]));
			}
			else if(arr[0].equals("print"))
			{
				systemcall.print(x.variables.get(arr[1]));
			}
			//inputTerminal
			else if(arr[0].equals("input"))
			{
				systemcall.input(x);
			}
			//memory assignment
			else if(arr[0].equals("assign"))
			{
				systemcall.assign(x, arr[1]);
			}
			
		}
		
		return isBlocked;
		
	}
	

}
