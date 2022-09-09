package osp;

import java.util.*;
public class Resource {
	
	Queue<Process>blocked;
	Mutex m;
	String name;
	
	public Resource(String name)
	{
		this.name = name;
		
		blocked = new LinkedList<Process>();
		
		m = new Mutex();
	}

}






