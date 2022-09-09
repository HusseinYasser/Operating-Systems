import java.io.File;
import java.io.IOException;
import java.util.*;

public class OS {
	
	Parser parser;
	
	Scheduler scheduler;
	
	int clockCycles ;
	
	int arrivalTime[];
	
	SystemCall systemcall;
	
	boolean flag[];
	
	int finished ;
	
	Process tobePreempted;
	
	OS(int ticks, int arrivalTime[])
	{
		parser = new Parser();
		
		scheduler = new Scheduler(ticks);
		
		clockCycles = 1;
		
		this.arrivalTime = arrivalTime;
		
		systemcall = new SystemCall();
		
		flag = new boolean[2];
		
		finished = 3;
		
		tobePreempted = null;
		
	}
	
	
	public void run() throws IOException
	{
		
		create();
		if(tobePreempted!=null)
			scheduler.preempt(tobePreempted);
		Process h = scheduler.dispatch();
		int check = isThere(h);
		int start = -1;
		if(check == -1)
		{
			
			if(systemcall.read(0) == null)
			{
				Queue<String> newProcess = systemcall.readFromDisk();
				int size = newProcess.size();
				for(int i = 0 ; i < size; ++i)
				{
					String p = newProcess.poll();
					systemcall.write(i, p);
				}
				start = 0;
				
			}
			else if(systemcall.read(20) == null)
			{
				Queue<String> newProcess = systemcall.readFromDisk();
				int size = newProcess.size();
				for(int i = 20 ; i < size; ++i)
				{
					systemcall.write(i, newProcess.poll());
				}
				start = 20;
			}
			else
			{
				start = swap();
			}
		}
		else
		{
			start = check;
		}
		
		
		boolean finish = false;
		
		h.state = "Running";
		systemcall.write(start + 1, "State "+h.state);
		systemcall.write(start + 2, "PC "+((h.pc%20)+start));
		
		flag[(start == 0)? 0:1] = true;
		
		for(int i = 0 ; i < scheduler.ticks; ++i)
		{
			System.out.println("In Clock Cycle "+(clockCycles++)+": ");
			System.out.println("Process "+h.id +" is running now!");
			System.out.println("Process "+h.id + " is executing instrction "+ systemcall.read(h.pc));
			String instr = systemcall.read(h.pc);
			String []arr = instr.split(" ");
			
			//Execution Part
			if(arr[0].equals("assign") == true)
			{
				if(arr[2].equals("input") == true)
				{
					String inp = systemcall.input();
					for(int k = start + 5 ; k < start + 8 ; ++k)
					{
						if(systemcall.read(k) == null || systemcall.read(k).equals("null"))
						{
							systemcall.write(k, arr[1] + " "+inp);
							break;
						}
					}
				}
				else 
				{
					String fileName = "";
					for(int k = start + 5 ; k < start + 8 ; ++k)
					{
						String tmpp = systemcall.read(k);
						String arrr[] = tmpp.split(" ");
						if(arrr[0].equals(arr[3]))
						{
							fileName = arrr[1];
							break;
						}
							
					}
					String tmp = systemcall.readFile(fileName);
					for(int k = start + 5 ; k < start + 8 ; ++k)
					{
						if(systemcall.read(k)==null || systemcall.read(k).equals("null"))
						{
							systemcall.write(k+start, arr[1] + " "+tmp);
							break;
						}
					}
				}
			}
			else if(arr[0].equals("writeFile"))
			{
				String fileName = "";
				String x = "";
				for(int k = 5 ; k < 8 ; ++k)
				{
					String tmp = systemcall.read(start+k);
					String[]arr2 = tmp.split(" ");
					if(arr2[0].equals(arr[1]))
					{
						fileName = arr2[1];
						break;
					}
				}
				for(int k = 5 ; k < 8 ; ++k)
				{
					String tmp = systemcall.read(start+k);
					String[]arr2 = tmp.split(" ");
					if(arr2[0].equals(arr[2]))
					{
						x = arr2[1];
						break;
					}
				}
				systemcall.writeFile(fileName, x);
			}
			
			//Updating variables Part
			h.pc++;
			systemcall.write(start+2,"PC "+ h.pc+"");
			String ttt = systemcall.read(start + 4);
			String tarr[] = ttt.split(" ");
			if(h.pc >= Integer.parseInt(tarr[1])) {
				finish = true;
				for(int b = start ; b < Integer.parseInt(tarr[1]) ; ++b)
					systemcall.delete(b);
			}
			
			printings(h);
			if(finish)
				break;
		}
		flag[start == 0 ? 0:1] = false;
		h.state = "Ready";
		if(finish == false) systemcall.write(start + 1,"State " +h.state);
		if(finish != true)
		{
			
			tobePreempted = h;
			return;
		}
		tobePreempted = null;
		finished--;
	}
	
	public void printings(Process h) throws IOException
	{
		systemcall.printMemory();
		systemcall.printDisk();
		System.out.println("--------------------------------------------------------------------------------------");
	}
	
	
	
	public int swap() throws IOException
	{
		Queue<String>newProcess = systemcall.readFromDisk();
		
		int oldStart = -1;
		int oldEnd = -1;
		
		int newStart = -1;
		
		if(flag[0] == false)
		{
			oldStart = 0;
			String tmp =  systemcall.read(0 + 4);
			String [] arr = tmp.split(" ");
			oldEnd = Integer.parseInt(arr[1]);
			Queue<String> oldProcess = new LinkedList<String>();
			for(int i = oldStart ; i < oldEnd ; ++i)
			{
				if(systemcall.read(i) == null)
					oldProcess.add("null");
				else
					oldProcess.add(systemcall.read(i));
				systemcall.delete(i);
			}
			systemcall.writeOnDisk(oldProcess);
		}
		else
		{
			oldStart = 20;
			String tmp =  systemcall.read(20 + 4);
			String [] arr = tmp.split(" ");
			oldEnd = Integer.parseInt(arr[1]);
			Queue<String> oldProcess = new LinkedList<String>();
			for(int i = oldStart ; i < oldEnd ; ++i)
			{
				if(systemcall.read(i) == null)
					oldProcess.add("null");
				else
					oldProcess.add(systemcall.read(i));
			}
			systemcall.writeOnDisk(oldProcess);
		}
		newStart = oldStart;
		int size = newProcess.size();
		for(int i = 0 ; i < size; ++i)
			systemcall.write(newStart+i, newProcess.poll());
		systemcall.write(newStart+3, "LowerBound "+newStart+"");
		systemcall.write(newStart+4, "UpperBound "+(newStart + size)+"");
		String pc = systemcall.read(newStart+2);
		String arr[] = pc.split(" ");
		int pcc= Integer.parseInt(arr[1]);
		systemcall.write(newStart + 2 , ((pcc%20)+newStart)+"");
		return newStart;
	}
	
	
	
	public int isThere(Process h)
	{
		String str1 = systemcall.read(0);
		if(str1 != null) {
			String arr[] = str1.split(" ");
			if(Integer.parseInt(arr[1]) == h.id)
			{
				String huss1 = systemcall.read(3);
				return Integer.parseInt((huss1.split(" "))[1]);
			}
		}
		String str2 = systemcall.read(20);
		if(str2!=null)
		{
			String arr2[] = str2.split(" ");
			arr2 = str2.split(" ");
			if(Integer.parseInt(arr2[1]) == h.id) {
				String huss2 = systemcall.read(23);
				return Integer.parseInt((huss2.split(" "))[1]);
			}
				
		}
		return -1;
	}
	
	
	
	
	
	public void create() throws IOException
	{
		for(int i = 0 ; i < 3 ; ++i)
		{
			if(arrivalTime[i] == clockCycles)
			{
				Process n = createProcess(i+1);
				scheduler.ready.add(n);
			}
		}
	}
	
	
	
	
	public Process createProcess(int id) throws IOException
	{
		int start = -1;
		int end = -1;
		Queue<String> instructions = parser.parse(new File("C:\\Users\\admin\\\\Desktop\\OSPrograms\\Program_"+id+".txt"));
		if(systemcall.read(0) == null)
		{
			start = 0;
		}
		else if(systemcall.read(20) == null)
		{
			start = 20;
		}
		else
		{
			int newStart = -1,newEnd = -1;
			
			if(flag[0] == false)
			{
				start = 0;
				newStart = 0;
			}
			else
			{
				start = 20;
				newStart = 20;
			}
			String tmp = systemcall.read(newStart + 4);
			String arr[] = tmp.split(" ");
			newEnd = Integer.parseInt(arr[1]);
			Queue<String>oldProcess = new LinkedList();
			for(int i = newStart ; i < newEnd ; ++i)
			{
				oldProcess.add(systemcall.read(i)==null? "null":systemcall.read(i));
				systemcall.delete(i);
			}
			systemcall.writeOnDisk(oldProcess);
		}
		systemcall.write(start, "ID "+id);
		systemcall.write(start + 1, "State Ready");
		systemcall.write(start + 2, "PC "+8);
		systemcall.write(start + 3, "LowerBound "+start);
		end = start + 5 + 3 + instructions.size();
		systemcall.write(start+4, "UpperBound "+end);
		Process n = new Process(id, "Ready" ,start + 8, start , end);
		systemcall.loadToMemory(start, end, instructions, true);
		return n;
	}
	
	

}
