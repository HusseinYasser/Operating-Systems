package osp;

import java.io.IOException;
import java.util.*;

public class Scheduler {
	
	int osFinish=3;
	int ticks;
	
    int clockCycles = 1;
	
	OS os;
	
	public Scheduler(int ticks , OS os)
	{
		this.os = os;
		
		this.ticks = ticks;
		
		clockCycles = 1;
		
	}
	
	public void preempt(Process x)
	{
		
		os.Ready.add(x);
	
	}
	
	public void schedule() throws IOException
	{
		Process x = os.Ready.poll();
		
		if(x == null)
		{
			boolean c = os.admitProcess(clockCycles);
			if(c == true)
				return;
			clockCycles++;
			System.out.println("In Clock Cycle "+clockCycles+": ");
			System.out.print("General Ready Queue: ");
			for(Process h:os.Ready)
				System.out.print(h.id+" ");
			System.out.println();
			System.out.print("General Blocked Queue: ");
			for(Process h:os.Blocked)
				System.out.print(h.id+" ");
			System.out.println();
			for(Map.Entry<String, Resource> entry : os.resources.entrySet()) {
			    String key = entry.getKey();
			    Resource value = entry.getValue();
			    System.out.print(key +" Resource Blocked Queue: ");
			    for(Process h:value.blocked)
			    	System.out.print(h.id+" ");
			    System.out.println();
			}
			return ;
		}
		
		boolean isBlocked = false;
		
		for(int i = 0 ; i < ticks && x.instructions.size() > 0; ++i)
		{
			
			os.admitProcess(clockCycles);
			
			String instr = x.instructions.poll();
			
			// printings part
			System.out.println("In Clock Cycle "+clockCycles+": ");
			System.out.println("Process "+x.id+" is got scheduled to the processor");
			System.out.println("Process "+x.id+" is Executing Instruction name: "+instr);
			System.out.print("General Ready Queue: ");
			for(Process h:os.Ready)
				System.out.print(h.id+" ");
			System.out.println();
			System.out.print("General Blocked Queue: ");
			for(Process h:os.Blocked)
				System.out.print(h.id+" ");
			System.out.println();
			for(Map.Entry<String, Resource> entry : os.resources.entrySet()) {
			    String key = entry.getKey();
			    Resource value = entry.getValue();
			    System.out.print(key +" Resource Blocked Queue: ");
			    for(Process h:value.blocked)
			    	System.out.print(h.id+" ");
			    System.out.println();
			}
			
			
			clockCycles++;
			
			boolean check = os.execute(instr, x);
			isBlocked |= check;
			System.out.println("-------------------------------------------------");
			if(isBlocked)
				break;
			
			
		}
		
		if(x.instructions.size() == 0)
		{
			osFinish--;
			System.out.println("Program "+x.id+" has finished");
			System.out.println("-------------------------------------------------");
		}
		else if(isBlocked == false) {
			this.preempt(x);
		}
		
	}

}
