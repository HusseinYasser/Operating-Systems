package osp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.Collections;
import java.util.Scanner;

class Pair2 implements Comparable<Pair2>
{
	Process p ;
	int time;
	
	
	Pair2(Process p,int time)
	{
		this.p = p ;
		this.time = time;
	}
	

	@Override
	public int compareTo(Pair2 o) {
		// TODO Auto-generated method stub
		return time<o.time ? -1:1;
	}
}

public class Main {
	
	public static void main(String[]args) throws IOException
	{
		
		
		
		File program1 = new File("F:\\Program_1.txt");
		File program2 = new File("F:\\Program_2.txt");
		File program3 = new File("F:\\Program_3.txt");
		
		CodeParser cp = new CodeParser();
		
		//Creating the Processes in the milestone
		Process p1 = new Process(program1 , cp);
		Process p2 = new Process(program2 , cp);
		Process p3 = new Process(program3 , cp);
		
		//Creating the resources in the Milestone
		Resource inputTerminal = new Resource("userInput");
		Resource outputTerminal = new Resource("userOutput");
		Resource file = new Resource("file");
	
		
		OS windows = new OS();
		
		//admitting our resources that we have to the operating system
		
		windows.addResource(inputTerminal.name,inputTerminal);
		windows.addResource(outputTerminal.name , outputTerminal);
		windows.addResource(file.name, file);
		
		//Admitting Processes according to the time of admission the user chooses
		ArrayList<Pair2>submission = new ArrayList<Pair2>();
		Scanner m = new Scanner(System.in);
		for(int i = 1; i <= 3; ++i) {
			System.out.println("Enter the time of program "+i+" submission: ");
			int timeOfInput = m.nextInt();
			timeOfInput++;
			Pair2 tmp = new Pair2((i==1)? p1:(i==2)? p2:p3 , timeOfInput);
			submission.add(tmp);
		}
		System.out.println("Enter number of cycles for the scheduler");
		int cycles=m.nextInt();
		System.out.println("----------------------------");
		Collections.sort(submission);
		for(Pair2 p:submission)
			windows.UserInputProcess(p.p, p.time);
		
		//Runing the Processes itself
		Scheduler exec = new Scheduler(cycles,windows);
		
		while(true && exec.osFinish != 0)
			exec.schedule();
		
		
		
		
		

	}

}
