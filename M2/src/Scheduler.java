import java.util.*;

public class Scheduler {
	
	Queue<Process> ready;
	int ticks;
	
	Scheduler(int ticks)
	{
		
		this.ticks = ticks;
		
		ready = new LinkedList<Process>();
		
	}
	
	public Process dispatch()
	{
		return ready.poll();
	}
	
	public void preempt(Process x)
	{
		ready.add(x);
	}

}
